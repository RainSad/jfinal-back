/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.jfinal.plugin.activerecord.generator;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.dialect.Dialect;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.sql.DataSource;

public class MetaBuilder {
	protected DataSource dataSource;
	protected Dialect dialect = new MysqlDialect();
	protected Set<String> excludedTables = new TreeSet<String>(
			String.CASE_INSENSITIVE_ORDER);

	protected Connection conn = null;
	protected DatabaseMetaData dbMeta = null;

	protected String[] removedTableNamePrefixes = null;

	protected TypeMapping typeMapping = new TypeMapping();

	public MetaBuilder(DataSource dataSource) {
		if (dataSource == null) {
			throw new IllegalArgumentException("dataSource can not be null.");
		}
		this.dataSource = dataSource;
	}

	public void setDialect(Dialect dialect) {
		if (dialect != null)
			this.dialect = dialect;
	}

	public void addExcludedTable(String[] excludedTables) {
		if (excludedTables != null)
			for (String table : excludedTables)
				this.excludedTables.add(table);
	}

	public void setRemovedTableNamePrefixes(String[] removedTableNamePrefixes) {
		this.removedTableNamePrefixes = removedTableNamePrefixes;
	}

	public void setTypeMapping(TypeMapping typeMapping) {
		if (typeMapping != null)
			this.typeMapping = typeMapping;
	}

	public List<TableMeta> build() {
		System.out.println("Build TableMeta ...");
		List<TableMeta> ret = new ArrayList<TableMeta>();
		try {
			this.conn = this.dataSource.getConnection();
			this.dbMeta = this.conn.getMetaData();
			
			buildTableNames(ret);
			for (TableMeta tableMeta : ret) {
				buildPrimaryKey(tableMeta);
				buildColumnMetas(tableMeta);
			}
		} catch (SQLException e) {
		} finally {
			if (this.conn != null)
				try {
					this.conn.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
		}
		return ret;
	}

	protected boolean isSkipTable(String tableName) {
		return false;
	}

	protected String buildModelName(String tableName) {
		if (this.removedTableNamePrefixes != null) {
			for (String prefix : this.removedTableNamePrefixes) {
				if (tableName.startsWith(prefix)) {
					tableName = tableName.replaceFirst(prefix, "");
					break;
				}
			}

		}

		if (this.dialect instanceof OracleDialect) {
			tableName = tableName.toLowerCase();
		}

		return StrKit.firstCharToUpperCase(StrKit.toCamelCase(tableName));
	}

	protected String buildBaseModelName(String modelName) {
		return "Base" + modelName;
	}

	protected ResultSet getTablesResultSet() throws SQLException {
		String schemaPattern = (this.dialect instanceof OracleDialect) ? this.dbMeta
				.getUserName() : null;
		return this.dbMeta.getTables(this.conn.getCatalog(), schemaPattern,
				null, new String[] { "TABLE", "VIEW" });
	}

	protected void buildTableNames(List<TableMeta> ret) throws SQLException {
		ResultSet rs = getTablesResultSet();
		while (rs.next()) {
			String tableName = rs.getString("TABLE_NAME");


			if (isRemoveTable(tableName,excludedTables))  {
				System.out.println("Skip table :" + tableName);
			} else if (isSkipTable(tableName)) {
				System.out.println("Skip table :" + tableName);
			} else {
				TableMeta tableMeta = new TableMeta();
				tableMeta.name = tableName;
				tableMeta.remarks = rs.getString("REMARKS");

				tableMeta.modelName = buildModelName(tableName);
				tableMeta.baseModelName = buildBaseModelName(tableMeta.modelName);
				ret.add(tableMeta);
			}
		}
		rs.close();
	}
	protected boolean isRemoveTable(String tableName,Set<String> excludedTables ){
		boolean flag=false;
		for (String table: excludedTables) {
			if(tableName.startsWith(table)){
				flag=true;
				break;
			}
		}
		return flag;
	}
	protected void buildPrimaryKey(TableMeta tableMeta) throws SQLException {
		ResultSet rs = this.dbMeta.getPrimaryKeys(this.conn.getCatalog(), null,
				tableMeta.name);

		String primaryKey = "";
		int index = 0;
		while (rs.next()) {
			if (index++ > 0)
				primaryKey = primaryKey + ",";
			primaryKey = primaryKey + rs.getString("COLUMN_NAME");
		}
		tableMeta.primaryKey = primaryKey;
		rs.close();
	}

	protected void buildColumnMetas(TableMeta tableMeta) throws SQLException {
		String sql = this.dialect.forTableBuilderDoBuild(tableMeta.name);
		Statement stm = this.conn.createStatement();
		ResultSet rs = stm.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();

		for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
			ColumnMeta cm = new ColumnMeta();
			cm.name = rsmd.getColumnName(i);

			String colClassName = rsmd.getColumnClassName(i);
			String typeStr = this.typeMapping.getType(colClassName);
			if (typeStr != null) {
				cm.javaType = typeStr;
			} else {
				int type = rsmd.getColumnType(i);
				if ((type == -2) || (type == -3) || (type == 2004)) {
					cm.javaType = "byte[]";
				} else if ((type == 2005) || (type == 2011)) {
					cm.javaType = "java.lang.String";
				} else {
					cm.javaType = "java.lang.String";
				}

			}

			cm.attrName = buildAttrName(cm.name);

			tableMeta.columnMetas.add(cm);
		}

		rs.close();
		stm.close();
	}

	protected String buildAttrName(String colName) {
		if (this.dialect instanceof OracleDialect) {
			colName = colName.toLowerCase();
		}
		return StrKit.toCamelCase(colName);
	}
}
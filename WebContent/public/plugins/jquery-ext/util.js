var $u=$u||{};
/**************获取项目名******************/
$u.getRootPath=function(){  
    //获取当前网址，如： http://localhost:8083/share/index.jsp
    var curWwwPath=window.document.location.href;
    //获取主机地址之后的目录，如： /share/meun.jsp
    var pathName=window.document.location.pathname;
    var pos=curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083
    var localhostPaht=curWwwPath.substring(0,pos);
    //获取带"/"的项目名，如：/share
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    return (localhostPaht + projectName);
};
//获取页面传来的值
$u.getQueryString=function(sProp)  
{  
    var re = new RegExp("[&,?]"+sProp + "=([^//&]*)", "i");  
    var a = re.exec(document.location.search);  
    if (a == null)  
        return "";  
    return a[1];  
};
//解析uri传来的参数为json
$u.paresURI=function(uri){
   var patten=/([-\w]+)=([\w,%]+)/ig;
   var parames = {};
   uri.replace(patten, function(a, b, c){
       parames[b] = c;
   });
   return parames;
};
/*
 * true 为移动端,false为pc端
 * */
$u.browser=function() {
    var sUserAgent = navigator.userAgent.toLowerCase();
    var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
    var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
    var bIsMidp = sUserAgent.match(/midp/i) == "midp";
    var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
    var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
    var bIsAndroid = sUserAgent.match(/android/i) == "android";
    var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
    var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
    if (bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM) {
        document.writeln("phone");
    } else {
        document.writeln("pc");
    }
    return bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM;
};
//  对字符串进行Html编码。
$u.toHtmlEncode = function (str) {
    str = $u.isEmpty(str) ? "" : String(str);
    var temp = str;
    temp = temp.replace(/&/g, "&amp;");
    temp = temp.replace(/</g, "&lt;");
    temp = temp.replace(/>/g, "&gt;");
    temp = temp.replace(/\'/g, "&apos;");
    temp = temp.replace(/\"/g, "&quot;");
    temp = temp.replace(/\n/g, "<br />");
    temp = temp.replace(/\ /g, "&nbsp;");
    temp = temp.replace(/\t/g, "&nbsp;&nbsp;&nbsp;&nbsp;");
    return temp;
};

$u.isEmpty=function(value){
	if(value==null||value==undefined||String(value).replace(/(^\s*)|(\s*$)/g,"").length<=0){
		return true;
	}
	if(typeof(value)=="object"){
		 for (var item in value){
			 return false;
		 }
		 return true;
	}
};
$u.isNotEmpty=function(value){
	return !$u.isEmpty();
};
// 转换成全角
$u.toCase = function (str) {
    str = $u.isEmpty(str) ? "" : String(str);
    var tmp = "";
    for (var i = 0; i < str.length; i++) {
        if (str.charCodeAt(i) > 0 && str.charCodeAt(i) < 255) { 
        	tmp += String.fromCharCode(str.charCodeAt(i) + 65248); 
        }else { 
        	tmp += String.fromCharCode(str.charCodeAt(i)); 
        }
    }
    return tmp;
};
//  验证是否可以作为合法的用户名字符(字母开头，允许6-16字节，允许字母数字下划线)
$u.isUserName = function (str) {
    str = $u.isEmpty(str) ? "" : String(str);
    return /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/i.test(str);
};

//  判断当前 String 对象是否是正确的 身份证号码(中国) 格式。
$u.isIDCard = function (str) {
    str = $u.isEmpty(str) ? "" : String(str);
    var iSum = 0,
        sId = str,
        aCity = {
            11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古",
            21: "辽宁", 22: "吉林", 23: "黑龙江", 31: "上海", 32: "江苏",
            33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东",
            41: "河南", 42: "湖北", 43: "湖南", 44: "广东", 45: "广西",
            46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南",
            54: "西藏", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏",
            65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外"
        };
    if (!/^\d{17}(\d|x)$/i.test(sId)) {
        return false;
    }
    sId = sId.replace(/x$/i, "a");
    //非法地区
    if (aCity[parseInt(sId.substr(0, 2), 10)] == null) {
        return false;
    }
    var sBirthday = sId.substr(6, 4) + "-" + Number(sId.substr(10, 2)) + "-" + Number(sId.substr(12, 2)),
        d = new Date(sBirthday.replace(/-/g, "/"));
    //非法生日
    if (sBirthday != (d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate())) {
        return false;
    }
    for (var i = 17; i >= 0; i--) {
        iSum += (Math.pow(2, i) % 11) * parseInt(sId.charAt(17 - i), 11);
    }
    if (iSum % 11 != 1) {
        return false;
    }
    return true;
};
//  判断当前 String 对象是否是正确的电话号码格式(中国)。
$u.isTel = function (str) {
    str = $u.isEmpty(str) ? "" : String(str);
    return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(str);
};
//  判断当前 String 对象是否是正确的手机号码格式(中国)。
$u.isMobile = function (str) {
    str = $u.isEmpty(str) ? "" : String(str);
    return /^(13|14|15|17|18)\d{9}$/i.test(str);
};
//  判断当前 String 对象是否是正确的电话号码或者手机号码格式(中国)
$u.isTelOrMobile = function (str) {
    return $u.isTel(str) || $u.isMobile(str);
};

//  判断当前 String 对象是否是正确的 邮政编码(中国) 格式。
$u.isZipCode = function (str) {
    str = $u.isEmpty(str) ? "" : String(str);
    return /^[\d]{6}$/.test(str);
};
//  判断当前 String 对象是否是否存在汉字字符。
$u.existChinese = function (str) {
    str = $u.isEmpty(str) ? "" : String(str);
    //[\u4E00-\u9FA5]為漢字﹐[\uFE30-\uFFA0]為全角符號
    return !/^[\x00-\xff]*$/.test(str);
};
//  验证中文
$u.isChinese = function (str) {
    str = $u.isEmpty(str) ? "" : String(str);
    return /^[\u0391-\uFFE5]+$/i.test(str);
};
//  验证英文
$u.isEnglish = function (str) {
    str = $u.isEmpty(str) ? "" : String(str);
    return /^[A-Za-z]+$/i.test(str);
};
//  转换成日期。
$u.toDate = function (str) {
    str = coreString.isEmpty(str) ? "" : String(str);
    try { return new Date(str.replace(/-/g, "\/")); }
    catch (e) { return null; }
};
//判断当前 String 对象是否是正确的 电子邮箱地址(Email) 格式。
$u.isEmail = function (str) {
    str = $u.isEmpty(str) ? "" : String(str);
    return strEmail.search(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) != -1;
};

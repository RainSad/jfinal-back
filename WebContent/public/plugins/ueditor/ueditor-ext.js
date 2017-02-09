UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
UE.Editor.prototype.getActionUrl = function(action) {
	console.log(action);
	if(action == 'uploadimage'|| action == 'uploadscrawl'){
		return $u.getRootPath()+"/ueditor/uploadImage";
	}else if(action == 'uploadfile'){
		return $u.getRootPath()+"/ueditor/uploadFile";
	}else if(action == 'uploadvideo'){
		return $u.getRootPath()+"/ueditor/uploadVideo";
	}else if(action == 'listimage'){
		return $u.getRootPath()+"/ueditor/listImage";
	}else if(action == 'listfile'){
		return $u.getRootPath()+"/ueditor/listFile";
	}else {
		return this._bkGetActionUrl.call(this, action);
	}
}
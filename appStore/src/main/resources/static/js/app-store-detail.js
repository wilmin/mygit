//app-store-detail.html的数据加载
function appStoreDetailPageLoad() {
	var data = {
		"appUpgradeId": location.search.substring(location.search.indexOf("=") + 1)
	};
	$.ajax({
		type: "post",
		url: "app-Store-Detail",
		contentType : 'application/json',
		data: JSON.stringify(data),
		success: function(result) {
			console.log(result);
			var stat = result.modelVersion;
			var statApps = result.havApkList;
			/*var notStatApps = result.notApkList;*/
			var content = "";
			//加载添加静默安装apk名称下拉框
			/*for (var i = 0; i < notStatApps.length; i++) {
                $('#selectStoreDetailApkNameModel').append("<option value='" + notStatApps[i].apkid + "'>" + notStatApps[i].apkName + "</option>");
            }
            $('#selectStoreDetailApkNameModel').selectpicker('refresh');*/
			//加载文件信息
			content +=
				'<tr>' +
				  '<td>'+stat.model+'</td>' +
				  '<td id="android">'+stat.android+'</td>' +
 				  '<td>'+stat.customer+'</td>' +
				  '<input type="hidden" id="statId" value="'+stat.id+'">' +
				'</tr>';
			$("#storeDetailApkNameInfoTable").append(content);
			content = "";
			//加载策略信息
			for (var i = 0; i < statApps.length; i++) {
				if (statApps[i] != null) {
					content +=
						'<tr>' +
						  '<td>'+(i+1)+'</td>' +
						  '<td id="type'+statApps[i].apkid+'">'+statApps[i].apkName+'</td>' +
						  '<td id="type'+statApps[i].apkid+'">'+statApps[i].packageName+'</td>' +
						  '<td id="romVersion'+statApps[i].apkid+'">'+statApps[i].versionCode+'</td>';
					
						  content +=
						  '<td id="userID'+statApps[i].apkid+'">'+statApps[i].versionName+'</td>';
						 
						'</tr>';
				}
			}
			$("#storeDetailApkFilterTable").append(content);
		},
		error: function(result, xhr) {
			console.log(result);
			console.log(xhr);
		}
	});
}



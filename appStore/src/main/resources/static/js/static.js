//static-detail.html的数据加载
function staticDetailPageLoad() {
	var data = {
		"statId": location.search.substring(location.search.indexOf("=") + 1)
	};
	$.ajax({
		type: "post",
		url: "static-detail",
		contentType : 'application/json',
		data: JSON.stringify(data),
		success: function(result) {
			console.log(result);
			var stat = result.modelVersion;
			var statApps = result.statApkList;
			var content = "";
			//加载文件信息
			content +=
				'<tr>' +
				  '<td>'+stat.model+'</td>' +
				  '<td id="androidversion">'+stat.android+'</td>' +
 				  '<td>'+stat.customer+'</td>' +
				  '<input type="hidden" id="statId" value="'+stat.id+'">' +
				'</tr>';
			$("#infoTable").append(content);
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
			$("#filterTable").append(content);
			$(".filterEditBtn").bind("click",clickFilterEditBtn);
			$(".filterDeleteBtn").bind("click",clickFilterDeleteBtn);
		},
		error: function(result, xhr) {
			console.log(result);
			console.log(xhr);
		}
	});
}


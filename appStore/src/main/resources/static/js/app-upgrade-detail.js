//app-upgrade-detail.html的数据加载
function appUpgradeDetailPageLoad() {
	var data = {
		"appUpgradeId": location.search.substring(location.search.indexOf("=") + 1)
	};
	$.ajax({
		type: "post",
		url: "appUpgrade-Detail",
		contentType : 'application/json',
		data: JSON.stringify(data),
		success: function(result) {
			console.log(result);
			var stat = result.appUpgradeInfo;
			var statApps = result.havApkList;
			var notStatApps = result.notApkList;
			var content = "";
			//加载添加apk名称下拉框
			for (var i = 0; i < notStatApps.length; i++) {
                $('#selectApkNameModel').append("<option value='" + notStatApps[i].apkid + "'>" + notStatApps[i].apkName + "-" + notStatApps[i].versionName + "</option>");
            }
            $('#selectApkNameModel').selectpicker('refresh');
            //描述信息
            $('#desc').text(stat.description);
			//加载文件信息
			content +=
				'<tr>' +
				  '<td>'+stat.name+'</td>' +
				  '<td id="android">'+stat.android+'</td>' +
 				  /*'<td>'+stat.customer+'</td>' +*/
				  '<input type="hidden" id="statId" value="'+stat.id+'">' +
				'</tr>';
			$("#apkNameInfoTable").append(content);
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
						 
						  content +=
						  '<td><div class="btn-group" role="group" aria-label="operation"><button type="button" class="btn btn-default btn-sm filterDeleteBtn"value="'+statApps[i].apkid+'">Delete</button></div></td>' +
						'</tr>';
				}
			}
			$("#apkFilterTable").append(content);
			$(".filterEditBtn").bind("click",clickFilterEditBtn);
			$(".filterDeleteBtn").bind("click",clickFilterDeleteBtn);
		},
		error: function(result, xhr) {
			console.log(result);
			console.log(xhr);
		}
	});
}

//点击删除静默安装关联apk按钮
function clickFilterDeleteBtn() {
	var id = $(this).val();
	var data = {
			"apkid": $(this).val(),
			"appUpgradeId":$('#statId').val()
		};
	if(confirm('Sure to delete this record?')){
		 $.ajax({
				type: "post",
				url: "delete-AppUpgradeList",
				contentType : 'application/json',
				data: JSON.stringify(data),
				success: function(result) {
					layer.msg(result.msg);
					if (result.code == 1) {
						$('button[value="'+id+'"]').parents("tr").remove();
					}
				},
				error: function(result, xhr) {
					console.log(result);
					console.log(xhr);
				}
		});
	}
}
//获取下拉框的值
function fun(){
    obj = document.getElementById("selectApkNameModel");
    str = "";
    for(i=0;i<obj.options.length;i++){
    	if(obj.options[i].value==0) continue;
        str += obj.options[i].value+",";
    }
    return str;
}

//=========================点击提交策略按钮===================
$("#addAPPUpgradeBtn").click(function(){
	var apkid = $("#selectApkNameModel").val();
	//判断按钮是否勾选,若勾选则选中所有apk添加
	if($("#checkAllApk").prop('checked')){
		//循环获取所有apk下拉框的编号值
		apkid = fun();
	}
	
	if (apkid==0) {
		alert("Please select the apk name for upgrade push!");
	}else {
		var data = {
			"apkid": apkid,//对应的apk编号
			"appUpgradeId":$('#statId').val()	//静默安装过滤的编号
		}
		
		$.ajax({
			type: "post",
			url: "add-AppUpgradeList",
			data: JSON.stringify(data),
			contentType : 'application/json',
			success: function(result){
				if (result.code == "1") {
					$('#addApkUpgradeFilterModal').modal('hide');
					location.href = "app-upgrade-detail.html?id=" + $('#statId').val();
				}
			},
			error: function(result, xhr) {
				console.log(result);
				console.log(xhr);
			}
		});
	
	}
	return false;
});

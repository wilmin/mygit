$(document).ready(function () {
	//截取URL判断是哪个页面
	var beginIndex = location.pathname.indexOf("/", 1);
	var endIndex = location.pathname.indexOf(".", 0);
	var pageName = location.pathname.substring(beginIndex + 1, endIndex);
	
	switch (pageName) {
		case "store-dis":
			storePageLoad();		//apk列表
			break;
		case "store-detail":
			storeDetailPageLoad();	//自动安装
			break;
		case "static-detail":
			staticDetailPageLoad();	//静默安装
			break;
		case "app-upgrade-detail":
			appUpgradeDetailPageLoad();	//APP商城升级推送
			break;
		case "app-store-detail":
			appStoreDetailPageLoad();	//点击APP商城详情按钮查看apk信息
			break;
		case "store-add":
			requestApkTypeAll();
			break;
		case "store-addsuccess":
		break;
	}
});

//apk类型集合
function requestApkTypeAll(){
	$.ajax({
        type: "get",
        url: "getAppTypeAll",
        data: {
            //
        },
        success: function (result) {
            for (var i = 0; i < result.length; i++) {
            	if(result[i].status == 0) continue;
                $('#apkType').append("<option value='" + result[i].id + "'>" + result[i].name + "</option>");
            }
            $('#apkType').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
}

function confirm_selectWithFilterClick() {
    $('#store_table').bootstrapTable('refresh');
}
function detailStoreClick() {
	var selected = JSON.stringify($('#store_table').bootstrapTable('getSelections'));
    selected = $.parseJSON(selected);
    if (selected.length == 1) {
    	window.location.href="store-detail.html?id=" + selected[0].id;
    } else {
        layer.msg("Please Choose A Record!",{icon:2});
    }
	
}
function statusFormatter(value,row,index) {
	if(value=="Open") {
		return '<font color="green">'+'Open'+'</font>';
	} else if(value="Close") {
		return '<font color="red">'+'Close'+'</font>';
	}
	
}
function timeFormatter(value,row,index) {
	var date = new Date(value);
	var updateTime = date.getFullYear() +"-"+ (parseInt(date.getMonth()) + 1) +"-"+ date.getDate()+" " +date.getHours() + ":" +date.getMinutes()+":"+date.getSeconds();
	return updateTime;
}
function OperationFun(value,row,index) {
	return ['<button type="button" class="btn btn-warning btn-sm closeUpdateFile"><span class="glyphicon glyphicon glyphicon-off"></span>Cloes</button>',
	        '<button type="button" class="btn btn-warning btn-sm unForceUpdateFile"><span class="glyphicon glyphicon glyphicon-hand-down"></span>Unforce</button>'].join('');	
}
window.operateEvents = {
        'click .closeUpdateFile': function (e, value, row, index) {
        	layer.alert('This Function is undefined',{icon:4});
        },
        'click .unForceUpdateFile': function (e, value, row, index) {
        	layer.alert('This Function is undefined',{icon:4});
        }
}
function enforceFormatter(value,row,index) {
	if(value=="Yes") {
		return '<font color="green">'+'Yes'+'</font>';
	} else if(value="No") {
		return '<font color="red">'+'No'+'</font>';
	}
	
}

function apkdownloadFormatter(value,row,index) {
	return '<a href='+ value+'>'+'<i class="glyphicon glyphicon-download-alt"></i>'+'</a>';
}

/**
 * 显示apk信息列表信息
 * @returns
 */
function queryStoreInfo() {
    $('#store_table').bootstrapTable({
    	height:720,
    	search:true,
//    	showRefresh:true,
//    	showToggle:true,
    	showColumns:true,
    	events: operateEvents,
        pagination: true,
        sidePagination: 'client',
        pageNumber: 1,
        pageSize: 20,
        pageList: [10, 20, 30, 50],
        toolbar: $('#toolbar'),
       // uniqueId: 'id',
        url: 'store-info',
        onCheck: function () {
            buttonControl('#store_table', '#editStore', '#deleteStore', '#detailStore');
        },
        onCheckAll: function () {
            buttonControl('#store_table', '#editStore', '#deleteStore', '#detailStore');
        },
        onUncheckAll: function () {
            buttonControl('#store_table', '#editStore', '#deleteStore', '#detailStore');
        },
        onUncheck: function () {
            buttonControl('#store_table', '#editStore', '#deleteStore', '#detailStore');
        }
    });
}
function clickDeleteStore() {
	var selected = JSON.stringify($('#store_table').bootstrapTable('getSelections'));
    selected = $.parseJSON(selected);
    if(selected.length > 0) {
    	layer.msg('Sure to delete this record?', {
    		  time: 0 //不自动关闭
    		  ,btn: ['Confirm', 'Cancel']
    		  ,yes: function(index){
    		    layer.close(index);
    		    for (var i = 0; i < selected.length; i++) {
    	            $.ajax({
    	                type: "post",
    	                url: "deleteAppFile",
    	                contentType: 'application/json',
    	                data: JSON.stringify({id: selected[i].id}),
    	                success: function (result) {
    	                    if (result.code == 1) {
    	                        layer.msg("Delete Successfully!");
    	                        $('#store_table').bootstrapTable('refresh');
    	                    }
    	                },
    	                error: function (result, xhr) {
    	                    console.log(result);
    	                    console.log(xhr);
    	                }
    	            })
    	        }
    		  }});

    	
    } else {
    	layer.msg('Please choose a record!',{icon: 5});
    }
}
function selectCustomerOnchange() {
	//获取被选中的option标签选项
	if($('#selectCustomer').val() == "Please Select!") {
		var select = document.getElementById("selectGroup");
        select.options.length = 0;
		$("#selectGroup").attr("disabled", true);
	} else {
		$("#selectGroup").attr("disabled", false);
		query_patchadd_Group();
	}
//	     alert($('#selectCustomer').val());
	}

function query_patchadd_Group() {
    $.ajax({
        type: "get",
        url: "appLog_group-info",
        data: {
            "customer": $('#selectCustomer').val()
        },
        success: function (result) {
            var select = document.getElementById("selectGroup");
            select.options.length = 0;
            var data = $.parseJSON(result).otaGroup;
//            	$("#confirm_selectDeviceWithFilter").attr("disabled", false);
            $('#selectGroup').append("<option value='Please Select!'>" + "Please Select!" + "</option>");
            if(data == undefined) {
            	$('#selectGroup').append("<option value='No Group'>" + "No Group" + "</option>");
            }
            for (var i = 0; i < data.length; i++) {
                $('#selectGroup').append("<option value='" + data[i].id + "'>" + data[i].groupname + "</option>");
            }
            $('#selectGroup').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
}
//store.html的数据加载
function storePageLoad() {
	queryStoreInfo();
	
	$('#confirm_selectWithFilter').bind("click", confirm_selectWithFilterClick);
	$('#detailStore').bind("click", detailStoreClick);
	$('#deleteStore').bind("click", clickDeleteStore);
}
//store-detail.html的数据加载
function storeDetailPageLoad() {
	var data = {
		"autoId": location.search.substring(location.search.indexOf("=") + 1)
	};
	$.ajax({
		type: "post",
		url: "auto-detail",
		contentType : 'application/json',
		data: JSON.stringify(data),
		success: function(result) {
			console.log(result);
			var auto = result.modelVersion;
			var autoApps = result.auotApkList;
			var content = "";
			//加载文件信息
			content +=
				'<tr>' +
				  '<td>'+auto.model+'</td>' +
				  '<td id="androidversion">'+auto.android+'</td>' +
 				  '<td>'+auto.customer+'</td>' +
				  '<input type="hidden" id="autoId" value="'+auto.id+'">' +
				'</tr>';
			$("#infoTable").append(content);
			content = "";
			//加载策略信息
			for (var i = 0; i < autoApps.length; i++) {
				if (autoApps[i] != null) {
					content +=
						'<tr>' +
						  '<td>'+(i+1)+'</td>' +
						  '<td id="type'+autoApps[i].apkid+'">'+autoApps[i].apkName+'</td>' +
						  '<td id="type'+autoApps[i].apkid+'">'+autoApps[i].packageName+'</td>' +
						  '<td id="romVersion'+autoApps[i].apkid+'">'+autoApps[i].versionCode+'</td>';
					
						  content +=
						  '<td id="userID'+autoApps[i].apkid+'">'+autoApps[i].versionName+'</td>';
						 
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

//点击删除自动配置关联apk按钮
function clickFilterDeleteBtn() {
	var id = $(this).val();
	if(confirm('Sure to delete this record?')){
		 $.ajax({
				type: "post",
				url: "deleteAutoApp",
				contentType : 'application/json',
				data: JSON.stringify(
				{
					"apkid": $(this).val(),
					"autoid":$('#autoId').val()
				}),
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


//点击添加apk文件按钮
$("#addApk").click(function(){
	location.href = "store-add.html";
});
//多选apk按钮
$("#selectBtn").click(function(){
	$("input.selectFilter[type='checkbox']").removeClass("displayNone");
	$("#bulkAddFilterBtn").parents("tr").removeClass("displayNone");
});
//点击取消多选按钮
function clickCanceltBtn(){
	$("input.selectFilter[type='checkbox']").addClass("displayNone");
	$("#bulkAddFilterBtn").parents("tr").addClass("displayNone");
	$("input.selectFilter[type='checkbox']").prop("checked", false);
}
//点击添加策略按钮
function clickBulkAddFilterBtn(){
	var checkedBox = $("input.selectFilter:checked");
	if (checkedBox.length > 0) {
		var checkedValue = '';
		for (var i = 0; i < checkedBox.length - 1; i++) {
			checkedValue += checkedBox[i].value + ',';
		}
		checkedValue += checkedBox[i].value;
		$("#apkid").val(checkedValue);
		
		$("#addFilterModal").modal("show");
	} else {
		alert("You don't select any file!");
	}
}
//点击删除文件按钮
function clickDeleteFile() {
	layer.msg('Sure to delete this record?', {
		  time: 0 //不自动关闭
		  ,btn: ['Confirm', 'Cancel']
		  ,yes: function(index){
		    layer.close(index);
		    var id = $(this).val();
			$.ajax({
				type: "post",
				url: "deleteAppFile",
				contentType : 'application/json',
				data: JSON.stringify({id: $(this).val()}),
				success: function(result) {
					if (result.code == 1) {
						alert("Delete Successfully!");
						$('button[value="'+id+'"]').parents("tr").remove();
					}
				},
				error: function(result, xhr) {
					console.log(result);
					console.log(xhr);
				}
			});
		  }});

	
}
$("#back").click(function(){
	window.history.back();
});

//文件上传
$("#start-upload").click(uploadFile);
function uploadFile(){
	var fileObj = document.getElementById("updateFile").files[0]; // 获取文件对象
    if(fileObj){
    	console.log(fileObj);
      // FormData 对象
      var form = new FormData();
      form.append("file", fileObj);// 文件对象
      // XMLHttpRequest 对象
      console.log(form);
      var xhr = new XMLHttpRequest();

      // 接收上传文件的后台地址
      xhr.open("post", "uploadappstorefile", true);
      xhr.onload = function () {
        xhr.responseText;
      };
      // 每当readyState发生改变的时候，onreadystatechange函数就会被执行
      xhr.onreadystatechange = function() {
      	if (xhr.readyState == 4) {
      		alert("File update successfully!");
      		console.log($("#start-upload").val());
      		$("#start-upload").val(1);
      		console.log($("#start-upload").val());
      	}
      }
      xhr.send(form);
    }else{
      alert("Please choose a file!");
    }
  return false;
}
//图标上传
$("#start-upload2").click(uploadIcon);
function uploadIcon(){
	var fileObj = document.getElementById("updateIcon").files[0]; // 获取文件对象
    if(fileObj){
    	console.log(fileObj);
      // FormData 对象
      var form = new FormData();
      form.append("file", fileObj);// 文件对象
      // XMLHttpRequest 对象
      console.log(form);
      var xhr = new XMLHttpRequest();

      // 接收上传文件的后台地址
      xhr.open("post", "uploadIconfile", true);
      xhr.onload = function () {
        xhr.responseText;
      };
      // 每当readyState发生改变的时候，onreadystatechange函数就会被执行
      xhr.onreadystatechange = function() {
      	if (xhr.readyState == 4) {
      		alert("File update successfully!");
      		console.log($("#start-upload2").val());
      		$("#start-upload2").val(1);
      		console.log($("#start-upload2").val());
      	}
      }
      xhr.send(form);
    }else{
      alert("Please choose a file!");
    }
  return false;
}
//store-add.html数据提交按钮
$("#submit").click(function(){
	 //&& $("#alreadyUploadIcon").val()=="1" $("#appStoreVersion").val() && 
	//路径
	var url = null;
	//判断第三方连接按钮是否选中
	if($("input[type='checkbox']").prop('checked')){
		if ($("#apkdownload").val()) {
			url =  $("#apkdownload").val();
			$("#alreadyUploadFile").val('1');
		}else{
			$("#alreadyUploadFile").val('0');
			layer.alert("Please fill in the correct download link address!",{icon:2});
			return false;
		}
	}

	//判断apk文件上传文件
	if(($("#alreadyUploadFile").val()=="0")) {
		layer.alert("You did not upload the apk installation package!",{icon:2});
		return false;
	}

	//判断字段填写完整性
	if ($("#apkName").val() && $("#apkVersion").val() && $("#apkVersionName").val()
			&& $("#packageName").val() && $("#apkprofile").val() && $("#apkType").val()) {
		//字段填充补全
		var data = {
				"apkname": $("#apkName").val(),
				"apkversionname": $("#apkVersionName").val(),
				"apkversion": $("#apkVersion").val(),
				"apkpackagename": $("#packageName").val(),
				"apkprofile": $("#apkprofile").val(),
				"apktype": $("#apkType").val(),
				"apkinfo": $("#description").val(),
				"apkdownload": url,
			}
			$.ajax({
				type: "post",
				url: "addapkupdateFile",
				contentType : 'application/json',
				data: JSON.stringify(data),
				success: function(result){
					if (result.code == "1") {
							layer.alert("Submit File Success!");
							location.href = "store-dis.html";
						}
				},
				error: function(result, xhr) {
					console.log(result);
					console.log(xhr);
				}
			});
	
	} else {
		console.log($("#apkVersionName").val());
		console.log($("#apkVersion").val());
		layer.alert("Items marked with a red star are required!",{icon:2});
	}

	
	return false;
});

//=========================点击提交策略按钮===================
$("#addFilterBtn").click(function(){
	if ($("#selectModel").val()==0) {
		alert("Please select the associated apk name for automatic configuration!");
	}else {
		var data = {
			"apkid": $("#selectModel").val(),//对应的apk编号
			"autoid":$('#autoId').val()	//自动配置过滤的编号
		}
		
		$.ajax({
			type: "post",
			url: "addAutoApp",
			data: JSON.stringify(data),
			contentType : 'application/json',
			success: function(result){
				if (result.code == "1") {
					$('#addFilterModal').modal('hide');
					location.href = "store-detail.html?id=" + $('#autoId').val();
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
//点击批量广告提交策略按钮
$("#bulk-addFilterBtn").click(function(){
	if ($("#romVersion").val() && $("#type").val()) {
		var data = {
			"ids": $("#apkid").val(),//对应的文件的id
			"Romversion": $("#romVersion").val(),
			"Type": $("#type").val(),
			"AppVersion": $("#appVersion").val(),
			"hardwareversion": $("#hardwareVersion").val(),
			"manufacturer": $("#manufacturer").val(),
			"DVBSupport": $("input[name='DVBSupport']:checked").val(),
			"HomeUI": $("#homeUI").val(),
			"UserID": $("#userID").val(),
			"customName": $("#customName").val(),
			"ipbegin": $("#IPAddressBegin").val(),
			"ipend": $("#IPAddressEnd").val()
		}
		$.ajax({
			type: "post",
			url: "apkstoreAddMultFilter",
			data: JSON.stringify(data),
			contentType : 'application/json',
			success: function(result){
				if ($.parseJSON(result).code == "1") {
					//如果mappingid不为空，则是修改策略
					alert("Submit File Success!");
					$('#addFilterModal').modal('hide');
				}
			},
			error: function(result, xhr) {
				console.log(result);
				console.log(xhr);
			}
		});
	} else {
		console.log("input.val(romVersion, type)");
		console.log($("#romVersion").val());
		console.log($("#type").val());
		alert("Items marked with a red star are required!");
	}
	return false;
});
//对话框在隐藏之后
$("#addFilterModal").on("hidden.bs.modal", function() {
	if (typeof $("#addFilterBtn")[0] != "undefined") {
		$("#addFilterBtn")[0].innerHTML = "Add";
		$("#addFilterTitle")[0].innerHTML = "Add Automatic Configuration App";
	}
	clearCheckForm();
});
//点击编辑策略按钮
function clickFilterEditBtn() {
	$("#addFilterBtn")[0].innerHTML = "Update";
	$("#addFilterTitle")[0].innerHTML = "Edit filter";
	$('#addFilterModal').modal('show');
	var num = $(this).val();
	$("#id").val($("#id").val());
	$("#mappingid").val(num);
	//填充input元素
	fillInputElement("romVersion", $("#romVersion"+num)[0].innerHTML);
	fillInputElement("type", $("#type"+num)[0].innerHTML);
	fillInputElement("userID", $("#userID"+num)[0].innerHTML);
	
}

//填充对应的input元素
function fillInputElement(name, value) {
	if (value != "") {
		if ($("#"+name+"Check").length > 0) {
			$("#"+name+"Check")[0].checked = true;
		}
		$("#"+name+"Div").removeClass("displayNone");
		$("#"+name).val(value);
	} else {
		//
	}
}
//清理勾选表单，隐藏input
function clearCheckForm() {
	var checkboxArray = $('.filterForm input[type="checkbox"]');
	$('.filterForm input[type!="checkbox"][type!="radio"]').val("");
	for (var i = 0; i < checkboxArray.length; i++) {
		checkboxArray[i].checked = false;
		$("#" + checkboxArray[i].value).addClass("displayNone");
	}
}



//////////////////////////////////////////////////////////////
//文件修改时
$("#uploadFile").change(function() {
    $("#btnUploadFile").val("upload files");
    $("#progressBarFile").width("0%");
    var file = $(this).prop('files');
    if (file.length != 0) {
        $("#batchUploadFileBtn").attr('disabled', false);
    }

});


// 上传按钮点击事件
$("#btnUploadFile").click(function() {
    // 进度条归零
    $("#progressBarFile").width("0%");
    // 进度条显示
    $("#progressBarFile").parent().show();
    $("#progressBarFile").parent().addClass("active");
    
    var fileSplit = $("#uploadFile").val().split('.');
    var length = fileSplit.length;
    if(fileSplit[length-1] == "apk") {
    	 //上传按钮修改为可用
        $(this).attr("disabled", true);
        uploadFunction();
    } else {
    	alert('Upload file format error,Please choose another file!');
    }
})

// 弹出上传Model
$("#activeUploadFile").click(function(){
	 $("#uploadFile").val("");
		$("#progressBarFile").width("0%");
		 $("#progressBarFile").parent().hide();
    $("#uploadFileModal").modal("show");
})


// 文件修改时
$("#uploadFile").change(function() {
    $("#btnUploadFile").val("upload files");
    var file = $(this).prop("files");
    if (file.length != 0) {
        $("#btnUploadFile").attr("disabled", false);
    }
});

//上传apk安装包
function uploadFunction() {
    var uploadFile = $("#uploadFile").get(0).files[0]; //获取文件对象
    
    // FormData 对象
    var form = new FormData();
    form.append("file", uploadFile); // 文件对象
    var uploadUrl = "store_uploadfile";//异步上传地址
    
    $.ajax({
        'type': "POST",
        'url': uploadUrl,
        'data': form,
        
        async: true,
        cache: false,
        contentType: false,
        processData: false,
        
        xhr: function(){ 
        	//获取ajaxSettings中的xhr对象，为它的upload属性绑定progress事件的处理函数
            myXhr = $.ajaxSettings.xhr();
           
            if(progressFunctionFile && myXhr.upload) { //检查进度函数和upload属性是否存在
                //绑定progress事件的回调函数
                myXhr.upload.addEventListener("progress",progressFunctionFile, false);
            }
            return myXhr; //xhr对象返回给jQuery使用
        },
        'error': function(request) {
        	/*result = $.parseJSON(result);*/
        	
            alert("Connection error");
            $("#alreadyUploadFile").val("0");
        },
        'success': function(data) {
        	//判断文件已存在服务器中
        	if(data.msgcode==0){
        		layer.alert("The file is already in the server!!");
        		return;
        	}else if(data.msgcode==-1){
        		layer.alert("There is a problem with file upload!");
        		return;
        	}
        	layer.alert("Upload Success!");
        	//显示apk安装名称、版本名称、版本号、包名        	
        	 $("input[name=apkName]").val(data.ApkInfo.apkName);
        	 $("input[name=apkVersionName]").val(data.ApkInfo.versionName);
        	 $("input[name=apkVersion]").val(data.ApkInfo.versionCode);
        	 $("input[name=packageName]").val(data.ApkInfo.packageName);
        	 //apk安装包上传成功后获取数据填充，输入框禁止修改
        	 document.getElementById("apkName").readOnly=true;
        	 document.getElementById("apkVersionName").readOnly=true;
        	 document.getElementById("apkVersion").readOnly=true;
        	 document.getElementById("packageName").readOnly=true;
        	 
//            $("input[name=companyLicenseImg]").val(data);
//            $("#licenseImg").attr("src","<%=path%>"+data); //将后台返回图片路径设置给IMG，显示图片
//            $("#licenseImg").attr("width","100"); 
//            $("#activeUpload").val("重新上传");
        	$("#alreadyUploadFile").val("1");
            $("#btnUploadFile").attr("disabled", false);
            $("#btnUploadFile").val("upload");
            $("#uploadFileModal").modal("hide");
            
        },
       /* async: false, */ 
        cache: false,  
        contentType: false,  
        processData: false
    });
}

//进度条控制
function progressFunctionFile(evt) {
	var progressBar = $("#progressBarFile");
    if (evt.lengthComputable) {
        var completePercent = Math.round(evt.loaded / evt.total * 100)+ "%";
        $("#btnUploadFile").val("upload：" + completePercent);
        progressBar.width(completePercent);
    }
}


//////////////////////////////////////////////////////////////
//文件修改时
$("#uploadIcon").change(function() {
  $("#btnUploadIcon").val("UploadIcon");
  $("#progressBarIcon").width("0%");
  var file = $(this).prop('files');
  if (file.length != 0) {
      $("#batchUploadIconBtn").attr('disabled', false);
  }

});


//上传按钮点击事件
$("#btnUploadIcon").click(function() {
  // 进度条归零
  $("#progressBarIcon").width("0%");
  // 进度条显示
  $("#progressBarIcon").parent().show();
  $("#progressBarIcon").parent().addClass("active");
  
  var fileSplit = $("#uploadIcon").val().split('.');
  var length = fileSplit.length;
  if(fileSplit[length-1] == "jpg" || fileSplit[length-1] == "png" || fileSplit[length-1] == "JPG" || fileSplit[length-1] == "PNG") {
	//上传按钮修改为可用
	  $(this).attr("disabled", true);
	  uploadIconFunction();
  } else {
  	alert('Uploaded images currently support JPG,PNG!');
  }
})

//弹出上传Model
$("#activeUploadIcon").click(function(){
	 $("#uploadIcon").val("");
		$("#progressBarIcon").width("0%");
		 $("#progressBarIcon").parent().hide();
  $("#uploadModalIcon").modal("show");
})


//文件修改时
$("#uploadIcon").change(function() {
  $("#btnUploadIcon").val("UploadIcon");
  var file = $(this).prop("files");
  if (file.length != 0) {
      $("#btnUploadIcon").attr("disabled", false);
  }
});

//上传apk屏幕截图
function uploadIconFunction() {
  var uploadFile = $("#uploadIcon").get(0).files[0]; //获取文件对象

  // FormData 对象
  var form = new FormData();
  form.append("file", uploadFile); // 文件对象
  var uploadUrl = "store_uploadScreenshot";//异步上传地址
  $.ajax({
	  'type': "POST",
      'url': uploadUrl,
      'data': form,
      
      async: true,
      cache: false,
      contentType: false,
      processData: false,
      xhr: function(){ //获取ajaxSettings中的xhr对象，为它的upload属性绑定progress事件的处理函数
          myXhr = $.ajaxSettings.xhr();
         
          if(progressFunctionIcon && myXhr.upload) { //检查进度函数和upload属性是否存在
              //绑定progress事件的回调函数
              myXhr.upload.addEventListener("progress",progressFunctionIcon, false);
          }
          return myXhr; //xhr对象返回给jQuery使用
      },
      error: function(request) {
          alert("Connection error");
          $("#alreadyUploadIcon").val("0");
      },
      success: function(data) {
      	layer.alert("Upload Success!");
//          $("input[name=companyLicenseImg]").val(data);
//          $("#licenseImg").attr("src","<%=path%>"+data); //将后台返回图片路径设置给IMG，显示图片
//          $("#licenseImg").attr("width","100"); 
//          $("#activeUpload").val("重新上传");
      	$("#alreadyUploadIcon").val("1");
          $("#btnUploadIcon").attr("disabled", false);
          $("#btnUploadIcon").val("Upload");
          $("#uploadModalIcon").modal("hide");
      }
  });
}

//进度条控制
function progressFunctionIcon(evt) {
	var progressBar = $("#progressBarIcon");
  if (evt.lengthComputable) {
      var completePercent = Math.round(evt.loaded / evt.total * 100)+ "%";
      $("#btnUploadIcon").val("Uploading：" + completePercent);
      progressBar.width(completePercent);
  }
}





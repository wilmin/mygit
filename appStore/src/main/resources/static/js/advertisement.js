$(document).ready(function () {
	//截取URL判断是哪个页面
	var beginIndex = location.pathname.indexOf("/", 1);
	var endIndex = location.pathname.indexOf(".", 0);
	var pageName = location.pathname.substring(beginIndex + 1, endIndex);
	switch (pageName) {
		case "advertisementDis":
			advertisementPageLoad();
			break;
		case "advertisement-detail":
			advertisementDetailPageLoad();
			break;
		case "advertisement-add":
		break;
		case "advertisement-addsuccess":
		break;
	}
});

function queryCustomerList() {
    $.ajax({
        type: "get",
        url: "customer-info",
        data: {
            //
        },
        success: function (result) {
            var data = $.parseJSON(result);
            for (var i = 0; i < data.length; i++) {
                $('#selectCustomer').append("<option value='" + data[i].name + "'>" + data[i].name + "</option>");
            }
            $('#selectCustomer').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
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

function queryModelList() {
    $.ajax({
        type: "get",
        url: "model-info",
        data: {
            //
        },
        success: function (result) {
            var data = $.parseJSON(result);
            for (var i = 0; i < data.length; i++) {
                $('#selectModel').append("<option value='" + data[i].type + "'>" + data[i].type + "</option>");
            }
            $('#selectModel').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
}
function OperationFun(value,row,index) {
	return ['<button type="button" class="btn btn-warning btn-sm closeAdFile"><span class="glyphicon glyphicon glyphicon-off"></span>Cloes</button>',
	        '<button type="button" class="btn btn-warning btn-sm unForceAdFile"><span class="glyphicon glyphicon glyphicon-hand-down"></span>Unforce</button>'].join('');	
}

window.operateEvents = {
        'click .closeAdFile': function (e, value, row, index) {
        	layer.alert('This Function is undefined',{icon:4});
        },
        'click .unForceAdFile': function (e, value, row, index) {
        	layer.alert('This Function is undefined',{icon:4});
        }
}

function timeFormatter(value,row,index) {
	var date = new Date(value);
	var updateTime = date.getFullYear() +"-"+ (parseInt(date.getMonth()) + 1) +"-"+ date.getDate()+" " +date.getHours() + ":" +date.getMinutes()+":"+date.getSeconds();
	return updateTime;
}
function queryCustomerList2() {
    $.ajax({
        type: "get",
        url: "customer-info",
        data: {
            //
        },
        success: function (result) {
            var data = $.parseJSON(result);
            for (var i = 0; i < data.length; i++) {
                $('#selectCustomer').append("<option value='" + data[i].name + "'>" + data[i].name + "</option>");
            }
            $('#selectCustomer').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
}
//advertisement.html的数据加载
function advertisementPageLoad() {
	queryCustomerList();
	queryAdInfo();
	
	$('#confirm_selectWithFilter').bind("click", confirm_selectWithFilterClick);
	$('#detailAd').bind("click", detailPatchClick);
	$('#deleteAd').bind("click", clickDeleteFile);
}
function confirm_selectWithFilterClick() {
    $('#advertisement_table').bootstrapTable('refresh');
}
function detailPatchClick() {
	var selected = JSON.stringify($('#advertisement_table').bootstrapTable('getSelections'));
    selected = $.parseJSON(selected);
    if (selected.length == 1) {
    	window.location.href="advertisement-detail.html?id=" + selected[0].id;
    } else {
        layer.msg("Please Choose A Record!",{icon:2});
    }
	
}
function queryAdInfo() {
	$('#advertisement_table').bootstrapTable({
    	height:720,
    	search:true,
//    	showRefresh:true,
//    	showToggle:true,
    	showColumns:true,
    	events: operateEvents,
        pagination: true,
        queryParams: function (params) {
            return {
                customer: $('#selectCustomer').val(),
                pageSize: params.limit,
                offset: params.offset
            }
        },
        sidePagination: 'client',
        pageNumber: 1,
        pageSize: 20,
        pageList: [10, 20, 30, 50],
        toolbar: $('#toolbar'),
       // uniqueId: 'id',
        url: 'advertisement-info',
        onCheck: function () {
            buttonControl('#advertisment_table', '#deleteAd', '#detailAd');
        },
        onCheckAll: function () {
            buttonControl('#advertisment_table', '#deleteAd', '#detailAd');
        },
        onUncheckAll: function () {
            buttonControl('#advertisment_table', '#deleteAd', '#detailAd');
        },
        onUncheck: function () {
            buttonControl('#advertisment_table', '#deleteAd', '#detailAd');
        }
    });
}
function downloadFormatter(value,row,index) {
	return '<a href='+ value+'>'+value+'</a>';
}
//点击删除文件按钮
function clickDeleteFile() {
	var selected = JSON.stringify($('#advertisement_table').bootstrapTable('getSelections'));
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
	                  url: "deleteAdFile",
	                  contentType: 'application/json',
	                  data: JSON.stringify({id: selected[i].id}),
	                  success: function (result) {
	                      result = $.parseJSON(result);
	                      if (result.code == 1) {
	                          layer.msg("Delete Successfully!");
	                          $('#advertisement_table').bootstrapTable('refresh');
	                      }
	                  },
	                  error: function (result, xhr) {
	                      console.log(result);
	                      console.log(xhr);
	                  }
	              })
	          }
  		  }
    	});
    } else {
    	layer.msg('Please choose a record!',{icon: 5});
    }
}
//advertisement-detail.html的数据加载
function advertisementDetailPageLoad() {
	queryCustomerList();
	queryModelList();
	$('#selectCustomer').on('change', selectCustomerOnchange);
	var data = {
		"advertisementId": location.search.substring(location.search.indexOf("=") + 1)
	};
	$.ajax({
		type: "post",
		url: "advertisement-detail",
		contentType: 'application/json',
		data: JSON.stringify(data),
		success: function(result) {
			var data = $.parseJSON(result).advertisement;
			var filter = $.parseJSON(result).advertisementmapping;
			var content = "";
			var date = new Date(data.updateTime);
			var updateTime = date.getFullYear() +"-"+ (parseInt(date.getMonth()) + 1) +"-"+ date.getDate();
			var filename = getFileName(data.download);
			//加载文件信息
			content +=
				'<tr>' +
					'<td><a href="'+data.download+'">'+filename+'</a></td>' +
				  '<td id="adversion">'+data.adversion+'</td>' +
				  '<td><a href="'+data.gotourl+'">'+data.gotourl+'</a></td>' +
				  '<td>'+data.positionid+'</td>' +
				  '<td>'+updateTime+'</td>' +
				  '<input type="hidden" id="adid" value="'+data.id+'">' +
				  '<input type="hidden" id="businessId" value="'+data.businessid+'">' +
				'</tr>';
			$("#infoTable").append(content);
			content = "";
			//加载策略信息
			for (var i = 0; i < filter.length; i++) {
				if (filter[i] != null) {
					content +=
						'<tr>' +
						  '<td>'+(i+1)+'</td>' +
						  '<td id="type'+filter[i].id+'">'+filter[i].type+'</td>' +
						  '<td id="romVersion'+filter[i].id+'">'+filter[i].romversion+'</td>' +
						  '<td id="hardwareVersion'+filter[i].id+'">'+filter[i].groupName+'</td>';
						  
						/*'<td id="manufacturer'+filter[i].id+'">'+filter[i].manufacturer+'</td>' +
						  '<td id="appVersion'+filter[i].id+'">'+filter[i].appversion+'</td>' +
						  '<td id="homeUI'+filter[i].id+'">'+filter[i].homeui+'</td>';*/
					
						 /* if (typeof filter[i].dvbsupport != "undefined") {
						  	content += '<td id="DVBSupport'+filter[i].id+'">'+filter[i].dvbsupport+'</td>';
						  } else {
						  	content += '<td id="DVBSupport'+filter[i].id+'"></td>';
						  }*/
					
						  content +=
						  '<td id="userID'+filter[i].id+'">'+filter[i].userid+'</td>';
						  
						 /* if (typeof filter[i].ipbegin != "undefined" && filter[i].ipbegin != '') {
						  	content += '<td id="IPAddress'+filter[i].id+'">'+filter[i].ipbegin +"-<br>"+ filter[i].ipend+'</td>';
						  } else {
						  	content += '<td id="IPAddress'+filter[i].id+'"></td>';
						  }*/
						  
						  content +=
						  '<td><div class="btn-group" role="group" aria-label="operation">  <button type="button" class="btn btn-default btn-sm filterEditBtn" value="'+filter[i].id+'">Edit</button>  <button type="button" class="btn btn-default btn-sm filterDeleteBtn"value="'+filter[i].id+'">Delete</button></div></td>' +
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
//点击添加广告文件按钮
$("#addAdvertisement").click(function(){
	location.href = "advertisement-add.html";
});
//多选广告按钮
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
		$("#adid").val(checkedValue);
		$("#addFilterModal").modal("show");
	} else {
		alert("You don't select any file!");
	}
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
      xhr.open("post", "uploadadfile", true);
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

//advertisement-add.html数据提交按钮
$("#submit").click(function(){
	if ($("#adVersion").val() && $("#positionID").val() && $("#gotoURL").val()) {
		if($("#alreadyUpload").val()=="1") { 
			var data = {
					"adVersion": $("#adVersion").val(),
					"positionID": $("#positionID").val(),
					"gotoURL": $("#gotoURL").val(),
				}
				$.ajax({
					type: "post",
					url: "addadupdateFile",
					contentType : 'application/json',
					data: JSON.stringify(data),
					success: function(result){
						if ($.parseJSON(result).code == "1") {
								alert("Submit File Success!");
								location.href = "advertisementDis.html";
							}
					},
					error: function(result, xhr) {
						console.log(result);
						console.log(xhr);
					}
				});
		}  else {
			layer.msg("Please Upload File!",{icon:2});
		}
		
	} else {
		console.log($("#adVersion").val());
		console.log($("#positionID").val());
		console.log($("#gotoURL").val());
		layer.alert("You should fill the blank with red star!");
	}
	return false;
});


//=========================点击提交策略按钮===================
$("#addFilterBtn").click(function(){
	if (($("#romVersion").val()=="") &&  ($("#userID").val()=="") 
			&& ($("#selectModel").val()=="Please Select!") 
			&& ($("#selectCustomer").val()=="Please Select!")
			&& (($("#selectGroup").val()=="Please Select!")
					||($("#selectGroup").val()=="Please"))) {
		console.log("input.val(romVersion, type)");
		console.log($("#romVersion").val());
		console.log($("#type").val());
		alert("You have to choose at least one entry!");
	} else {
		var data = {
			"id": $("#adid").val(),//对应的文件的id
			"mappingid": $("#mappingid").val(),//如果是更新策略就有mappingid，新增策略则为空
			"Romversion": $("#romVersion").val(),
			"Type": $("#selectModel").val(),
			"UserID": $("#userID").val(),
			
			//按组数据
			"businessid": $("#selectCustomer").val(),
			"groupid": $("#selectGroup").val()
		}
		var url = "advertisementAddFilter";
		//如果mappingid不为空，则是修改策略
		if ($("#mappingid").val() != "") {
			url = "advertisementUpdateFilter";
		}
		$.ajax({
			type: "post",
			url: url,
			data: JSON.stringify(data),
			contentType : 'application/json',
			success: function(result){
				if ($.parseJSON(result).code == "1") {
					//如果mappingid不为空，则是修改策略
					if($("#mappingid").val() != "") {
						alert("Change Filter Success!");
					} else {
						alert("Submit File Success!");
					}
					$('#addFilterModal').modal('hide');
					location.href = "advertisement-detail.html?id=" + $("#adid").val();
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
			"ids": $("#adid").val(),//对应的文件的id
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
			url: "advertisementAddMultFilter",
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
		alert("You should fill the blank with red star!");
	}
	return false;
});
//对话框在隐藏之后
$("#addFilterModal").on("hidden.bs.modal", function() {
	if (typeof $("#addFilterBtn")[0] != "undefined") {
		$("#addFilterBtn")[0].innerHTML = "Add";
		$("#addFilterTitle")[0].innerHTML = "Add filter";
	}
	clearCheckForm();
});


/*$('#addFilterModal').on('show.bs.modal', function () {
	queryCustomerList();
	queryModelList();
	$('#selectCustomer').on('change', selectCustomerOnchange);
})*/
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
//点击删除策略按钮
function clickFilterDeleteBtn() {
	var id = $(this).val();
	$.ajax({
		type: "post",
		url: "deleteAdFilter",
		contentType : 'application/json',
		data: JSON.stringify(
		{
			id: $(this).val(),
			adid:$(adid).val()
		}),
		success: function(result) {
			result = $.parseJSON(result);
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




/////////////////////////////////////////////////




//文件修改时
$("#uploadFile").change(function() {
 $("#btnUpload").val("Upload");
 $("#progressBar").width("0%");
 var file = $(this).prop('files');
 if (file.length != 0) {
     $("#batchUploadBtn").attr('disabled', false);
 }

});


//上传按钮点击事件
$("#btnUpload").click(function() {
 // 进度条归零
 $("#progressBar").width("0%");
 // 进度条显示
 $("#progressBar").parent().show();
 $("#progressBar").parent().addClass("active");
	
 //上传按钮修改为可用
 $(this).attr("disabled", true);
 
 
 var fileSplit = $("#uploadFile").val().split('.');
 var length = fileSplit.length;
 if(fileSplit[length-1] == "txt" || fileSplit[length-1] == "png" || fileSplit[length-1] == "jpg" || fileSplit[length-1] == "bmp") {
     //上传按钮修改为可用
     $(this).attr("disabled", true);
     uploadFunction();
 } else {
 	alert('Upload file format error,Please choose another file!');
 }
})

//弹出上传Model
$("#activeUpload").click(function(){
	 $("#uploadFile").val("");
	$("#progressBar").width("0%");
	 $("#progressBar").parent().hide();
 $("#uploadModal").modal("show");
})


//文件修改时
$("#uploadFile").change(function() {
 $("#btnUpload").val("Upload");
 var file = $(this).prop("files");
 if (file.length != 0) {
     $("#btnUpload").attr("disabled", false);
 }
});

//文件上传
function uploadFunction() {
 var uploadFile = $("#uploadFile").get(0).files[0]; //获取文件对象

 // FormData 对象
 var form = new FormData();
 form.append("file", uploadFile); // 文件对象
 var uploadUrl = "uploadadfile";//异步上传地址
 $.ajax({
     cache: false,
     type: "POST",
     url: uploadUrl,
     contentType: false, 
     processData: false, 
     data: form,
     xhr: function(){ //获取ajaxSettings中的xhr对象，为它的upload属性绑定progress事件的处理函数
         myXhr = $.ajaxSettings.xhr();
         if(progressFunction && myXhr.upload) { //检查进度函数和upload属性是否存在
             //绑定progress事件的回调函数
             myXhr.upload.addEventListener("progress",progressFunction, false);
         }
         return myXhr; //xhr对象返回给jQuery使用
     },
     error: function(request) {
         alert("Connection error");
         $("#alreadyUpload").val("0");
     },
     success: function(data) {
     	layer.alert("Upload Success!");
//         $("input[name=companyLicenseImg]").val(data);
//         $("#licenseImg").attr("src","<%=path%>"+data); //将后台返回图片路径设置给IMG，显示图片
//         $("#licenseImg").attr("width","100"); 
//         $("#activeUpload").val("重新上传");
     	$("#alreadyUpload").val("1");
         $("#btnUpload").attr("disabled", false);
         $("#btnUpload").val("Upload");
         $("#uploadModal").modal("hide");
     }
     
 });
}

//进度条控制
function progressFunction(evt) {
	var progressBar = $("#progressBar");
 if (evt.lengthComputable) {
     var completePercent = Math.round(evt.loaded / evt.total * 100)+ "%";
     $("#btnUpload").val("Uploading：" + completePercent);
     progressBar.width(completePercent);
 }
}
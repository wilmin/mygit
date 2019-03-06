$(document).ready(function () {
	//截取URL判断是哪个页面
	var beginIndex = location.pathname.indexOf("/", 1);
	var endIndex = location.pathname.indexOf(".", 0);
	var pageName = location.pathname.substring(beginIndex + 1, endIndex);
	switch (pageName) {
		case "patch":
			patchPageLoad();
			break;
		case "patch-detail":
			patchDetailPageLoad();
			break;
		case "patch-add":
			patchAddPageLoad();
		break;
		case "patch-addsuccess":
		break;
	}
});

function patchAddPageLoad() {
	queryCustomerList();
	
	$('#selectCustomer').on('change', selectCustomerOnchange);
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
        url: "group-info",
        data: {
            "customer": $('#selectCustomer').val()
        },
        success: function (result) {
            var select = document.getElementById("selectGroup");
            select.options.length = 0;
            var data = $.parseJSON(result);
//            	$("#confirm_selectDeviceWithFilter").attr("disabled", false);
            $('#selectGroup').append("<option value='Please Select!'>" + "Please Select!" + "</option>");
            for (var i = 0; i < data.length; i++) {
                $('#selectGroup').append("<option value='" + data[i] + "'>" + data[i] + "</option>");
            }
            $('#selectGroup').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
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

function enforceFormatter(value,row,index) {
	if(value=="Yes") {
		return '<font color="green">'+'Yes'+'</font>';
	} else if(value="No") {
		return '<font color="red">'+'No'+'</font>';
	}
	
}
function downloadFormatter(value,row,index) {
		//图标为眼睛，查看图标按钮
		/*return '<a target="_blank" href='+ value+'>'+'<i class="glyphicon glyphicon-eye-open"></i>'+'</a>';*/
	//下载按钮图标，下载文件
	return '<a href='+ value+'>'+'<i class="glyphicon glyphicon-download-alt"></i>'+'</a>';
}

function queryPatchInfo() {
    $('#patch_table').bootstrapTable({
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
                model: $('#selectModel').val(),
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
        url: 'patch-info',
        onCheck: function () {
            buttonControl('#patch_table', '#editPatch', '#deletePatch', '#detailPatch');
        },
        onCheckAll: function () {
            buttonControl('#patch_table', '#editPatch', '#deletePatch', '#detailPatch');
        },
        onUncheckAll: function () {
            buttonControl('#patch_table', '#editPatch', '#deletePatch', '#detailPatch');
        },
        onUncheck: function () {
            buttonControl('#patch_table', '#editPatch', '#deletePatch', '#detailPatch');
        }
    });
}

function selectDetailCustomerOnchange() {
    queryGroup();
}
function queryGroup() {
$.ajax({
    type: "get",
    url: "appLog_group-info", //xwm_group-info
    data: {
        "customer": $('#selectCustomer').val()
    },
    success: function (result) {
        var select = document.getElementById("selectGroup");
        select.options.length = 0;
        var data = $.parseJSON(result).otaGroup;
//        	$("#confirm_selectDeviceWithFilter").attr("disabled", false);
        $('#selectGroup').append("<option value='0'>" + "Please Select!" + "</option>");
        if(data == undefined) {
        	$('#selectGroup').append("<option value='0'>" + "No Group" + "</option>");
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

//patch.html的数据加载
function patchPageLoad() {
	queryPatchInfo();
	
	$('#confirm_selectWithFilter').bind("click", confirm_selectWithFilterClick);
	$('#detailPatch').bind("click", detailPatchClick);
	$('#deletePatch').bind("click", clickDeleteFile);
}

function confirm_selectWithFilterClick() {
    $('#patch_table').bootstrapTable('refresh');
}
function detailPatchClick() {
	var selected = JSON.stringify($('#patch_table').bootstrapTable('getSelections'));
    selected = $.parseJSON(selected);
    if (selected.length == 1) {
    	window.location.href="patch-detail.html?id=" + selected[0].id;
    } else {
        layer.msg("Please Choose A Record!",{icon:2});
    }
	
}

//patch-detail.html的数据加载
function patchDetailPageLoad() {
	queryCustomerList();
	$('#selectCustomer').on('change', selectDetailCustomerOnchange);
	
	var data = {
		"patchId": location.search.substring(location.search.indexOf("=") + 1)
	};
	$.ajax({
		type: "post",
		url: "patch-detail",
		contentType : 'application/json',
		data: JSON.stringify(data),
		success: function(result) {
			var data = $.parseJSON(result).updatefile;
			var filter = $.parseJSON(result).filemapping;
			var businessObj = $.parseJSON(result).business;
			var content = "";
			var date = new Date(data.updateTime);
			var updateTime = date.getFullYear() +"-"+ (parseInt(date.getMonth()) + 1) +"-"+ date.getDate();
			var filename = getFileName(data.download);
			//加载文件信息
			content +=
				'<tr>' +
				  '<td><a href="'+data.download+'">'+filename+'</a></td>' +
				  '<td id="fileVersion">'+data.fileVersion+'</td>' +
				  '<td>'+updateTime+'</td>' +
				  '<input type="hidden" id="fileid" value="'+data.id+'">' +
				  '<input type="hidden" id="businessId" value="'+data.businessid+'">' +
				'</tr>';
			$("#infoTable").append(content);
			//$("#versionInfo").append(data.description);
			content = "";
			//加载策略信息
			if(filter != undefined){
				for (var i = 0; i < filter.length; i++) {
					if (filter[i] != null) {
						content +=
							'<tr>' +
							  '<td>'+(i+1)+'</td>' +
							  '<td id="group'+filter[i].id+'">'+filter[i].group+'</td>' +
							  '<td id="type'+filter[i].id+'">'+filter[i].model+'</td>' +
							  '<td id="avaliableVersion'+filter[i].id+'">'+filter[i].romVersion+'</td>' +
							  '<td id="isCompulsive'+filter[i].id+'">'+filter[i].isCompulsive+'</td>';
							  content +=
							  '<td id="userID'+filter[i].id+'">'+filter[i].userid+'</td>';
							  content +=
							  '<td><div class="btn-group" role="group" aria-label="operation">  <button type="button" class="btn btn-default btn-sm filterEditBtn" value="'+filter[i].id+'">Edit</button>  <button type="button" class="btn btn-default btn-sm filterDeleteBtn"value="'+filter[i].id+'">Delete</button></div></td>' +
							'</tr>';
					}
				}
			}
			$("#filterTable").append(content);
			$(".filterEditBtn").bind("click",clickFilterEditBtn);
			$(".filterDeleteBtn").bind("click",clickFilterDeleteBtn);
			$('#selectCustomer').append("<option value='" + businessObj.id + "'>" + businessObj.name + "</option>");
			/*queryGroup();*/
		},
		error: function(result, xhr) {
			console.log(result);
			console.log(xhr);
		}
	});
	
}




$("#addPatch").click(function(){
	location.href = "patch-add.html"
});

//点击删除文件按钮
function clickDeleteFile() {
	var selected = JSON.stringify($('#patch_table').bootstrapTable('getSelections'));
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
    	                url: "deleteXmlFile",
    	                contentType: 'application/json',
    	                data: JSON.stringify({id: selected[i].id}),
    	                success: function (result) {
	                        layer.msg(result.msg);
	                        $('#patch_table').bootstrapTable('refresh');
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
$("#back").click(function(){
//	window.history.back();
	window.location.href = "patch.html"
	
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
      xhr.open("post", "uploadupdatefile", true);
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
//patch-add.html数据提交按钮
$("#submit").click(function(){
	if ($("#fileVersion").val() && $("#addOTA_Device").val() && $("#addOTA_Android").val()) {
		if($("#alreadyUpload").val()=="1") {
			var data = {
					"firmware": $("#fileVersion").val(),
					"description": $("#description").val(),
					"device": $("#addOTA_Device").val(),
					"android":$("#addOTA_Android").val()
			}
			$.ajax({
				type: "post",
				url: "addUpdateXMLFile",
				contentType : 'application/json',
				data: JSON.stringify(data),
				success: function(result){
					alert(result.msg);
					location.href = "patch.html";
				},
				error: function(result, xhr) {
					console.log(result);
					console.log(xhr);
				}
			});
		
		} else {
			layer.msg("Please Upload File!",{icon:2});
		}
		
	} else {
		console.log("input.val(updateFile, fileVersion)");
		console.log($("#updateFile").val());
		console.log($("#fileVersion").val());
		console.log($("#alreadyUpload").val());
		layer.msg("You should fill the blank with red star!",{icon:2});
	}
	return false;
});

//对话框在隐藏之后
$("#addFilterModal").on("hidden.bs.modal", function() {
	$("#addFilterBtn")[0].innerHTML = "Add";
	$("#addFilterTitle")[0].innerHTML = "Add filter";
	clearCheckForm();
});


//点击编辑策略按钮
function clickFilterEditBtn() {
	var num = $(this).val();
	$("#addFilterBtn")[0].innerHTML = "Update";
	$("#addFilterTitle")[0].innerHTML = "Edit filter";
	$('#addFilterModal').modal('show');
	
	$("#id").val($("#id").val());
	$("#mappingid").val(num);
	
	//填充input元素
	fillInputElement("avaliableVersion", $("#avaliableVersion"+num)[0].innerHTML);
	fillInputElement("type", $("#type"+num)[0].innerHTML);
	fillInputElement("userID", $("#userID"+num)[0].innerHTML);
	
	var tem = $("#type"+num)[0].innerHTML;
	$("#selectModel option[value='"+tem+"']").attr("selected",true);
  
	//两个单选框的选择
	if ($("#isCompulsive"+num)[0].innerHTML == "true") {
		$("#yesUpdate").prop("checked", true);
		$("#noUpdate").prop("checked", false);
	} else {
		$("#yesUpdate").prop("checked", false);
		$("#noUpdate").prop("checked", true);
	}
	
}
//点击删除策略按钮
function clickFilterDeleteBtn() {
	
	var id = $(this).val();
	$.ajax({
		type: "post",
		url: "deleteFilter",
		contentType : 'application/json',
		data: JSON.stringify(
		{
			id: $(this).val(),
			fileid:$(fileid).val()
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



// 文件修改时
$("#uploadFile").change(function() {
    $("#btnUpload").val("Upload");
    $("#progressBar").width("0%");
    var file = $(this).prop('files');
    if (file.length != 0) {
        $("#batchUploadBtn").attr('disabled', false);
    }

});


// 上传按钮点击事件
$("#btnUpload").click(function() {
    // 进度条归零
    $("#progressBar").width("0%");
    // 进度条显示
    $("#progressBar").parent().show();
    $("#progressBar").parent().addClass("active");
    var fileSplit = $("#uploadFile").val().split('.');
    var length = fileSplit.length;
    if(fileSplit[length-1] == "xml" || fileSplit[length-1] == "XML") {
        //上传按钮修改为可用
        $(this).attr("disabled", true);
        uploadFunction();
    } else {
    	alert('Upload file format error,Please choose another file!');
    }

})

// 弹出上传Model
$("#activeUpload").click(function(){
	 $("#uploadFile").val("");
		$("#progressBar").width("0%");
		 $("#progressBar").parent().hide();
    $("#uploadModal").modal("show");
})


// 文件修改时
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
    var uploadUrl = "uploadXMLFilter";//异步上传地址
   
    $.ajax({
        type: "POST",
        url: uploadUrl,
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
        success: function(request) {
        	layer.alert(request.msg);
        	$("#alreadyUpload").val("1");
            $("#btnUpload").attr("disabled", false);
            $("#btnUpload").val("Upload");
            $("#uploadModal").modal("hide");
        },
        cache: false,
        contentType: false, 
        processData: false
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

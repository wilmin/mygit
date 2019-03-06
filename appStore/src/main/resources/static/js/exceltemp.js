var user;  
$(document).ready(function () {
    user = new User();  
    user.init(); 
});

//弹出上传Model
$("#addExcelTemp").click(function(){
	$("#uploadExcel").val("");
	$("#progressBar2").width("0%");
	$("#progressBar2").parent().hide();
    $("#uploadModal2").modal("show");
})


var User = function(){  
    this.init = function(){ 
        //模拟上传excel  
         $("#uploadEventBtn").unbind("click").bind("click",function(){  
             $("#uploadExcel").click();  
         });  
         $("#uploadExcel").bind("change",function(){  
             $("#uploadEventPath").attr("value",$("#uploadExcel").val());  
         });
         //点击上传按钮
         $("#uploadBtn2").unbind("click").bind("click",function(){
        	 var uploadPicture = $("#uploadPicture").val();
        	 var uploadExcel = $("#uploadExcel").val();
        	 if($("input[name=exceltempname]").val().trim()=="" || $("input[name=exceltempname]").val()==null){
        		 alert("Please fill in the file name");
        		 return;
        	 }
        	 
        	 if(uploadPicture == ''){  
                 alert("Please select the picture and upload it again!");
                 return;
             }else if((uploadPicture.lastIndexOf(".png")<0) && (uploadPicture.lastIndexOf(".jpg")<0)){//可判断以.png和.jpg结尾的图片
            	 alert("Upload picture files only!");
            	 return;
             }
             if(uploadExcel == ''){  
            	 alert("Please select excel and upload it again!");
             }else if(uploadExcel.lastIndexOf(".xls")<0){//可判断以.xls和.xlsx结尾的excel  
            	 alert("Upload Excel files only!"); 
             }else{
            	 // 进度条归零
        	    $("#progressBar2").width("0%");
        	    // 进度条显示
        	    $("#progressBar2").parent().show();
        	    $("#progressBar2").parent().addClass("active");
        	    var fileSplit = $("#uploadExcel").val().split('.');
        	    var length = fileSplit.length;
        	    $(this).attr("disabled", true);
                 var url =  'uploadAddExcelTemp';  
                 var formData = new FormData($("#batchUpload2")[0]);  
                 user.sendAjaxRequest(url,'POST',formData);  
             } 
         }); 
          
    };  
  
    this.sendAjaxRequest = function(url,type,data){  
        $.ajax({  
            url : url,  
            type : type,  
            data : data,
            xhr: function(){ //获取ajaxSettings中的xhr对象，为它的upload属性绑定progress事件的处理函数
                myXhr = $.ajaxSettings.xhr();
                if(progressFunction && myXhr.upload) { //检查进度函数和upload属性是否存在
                    //绑定progress事件的回调函数
                    myXhr.upload.addEventListener("progress",progressFunction, false);
                }
                return myXhr; //xhr对象返回给jQuery使用
            },
            success : function(result) { 
                layer.alert(result);
                $("#alreadyUpload2").val("1");
                $("#uploadBtn2").attr("disabled", false);
                $("#uploadBtn2").val("Upload");
                $("#uploadModal2").modal("hide");
            },  
            error : function() {  
            	layer.alert("excel上传失败");
                $("#alreadyUpload2").val("0");
                $("#uploadBtn2").attr("disabled", false);
                $("#uploadBtn2").val("Upload");
                $("#uploadModal2").modal("hide");
            },  
            cache : false,  
            contentType : false,  
            processData : false  
        });
        
    };  
} 


//文件修改时
$("#uploadExcel").change(function() {
    $("#uploadBtn2").val("Upload");
    $("#progressBar2").width("0%");
    var file = $(this).prop('files');
    if (file.length != 0) {
        $("#batchuploadBtn2").attr('disabled', false);
    }

});

//文件修改时
$("#uploadExcel").change(function() {
    $("#uploadBtn2").val("Upload");
    var file = $(this).prop("files");
    if (file.length != 0) {
        $("#uploadBtn2").attr("disabled", false);
    }
});

//进度条控制
function progressFunction(evt) {
	var progressBar = $("#progressBar2");
    if (evt.lengthComputable) {
        var completePercent = Math.round(evt.loaded / evt.total * 100)+ "%";
        $("#uploadBtn2").val("Uploading：" + completePercent);
        progressBar.width(completePercent);
    }
}

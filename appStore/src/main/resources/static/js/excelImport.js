var user;  
$(function(){ 
    user = new User();  
    user.init();  
}); 

var User = function(){  
      
    this.init = function(){  
        //模拟上传excel  
         $("#uploadEventBtn").unbind("click").bind("click",function(){  
             $("#uploadEventFile").click();  
         });  
         $("#uploadEventFile").bind("change",function(){  
             $("#uploadEventPath").attr("value",$("#uploadEventFile").val());  
         });
         //点击上传按钮
         $("#uploadBtn").unbind("click").bind("click",function(){  
        	 var uploadEventFile = $("#uploadEventFile").val();  
             if(uploadEventFile == ''){  
                 alert("Please select excel and upload it again!");  
             }else if(uploadEventFile.lastIndexOf(".xls")<0){//可判断以.xls和.xlsx结尾的excel  
                 alert("Upload Excel files only!");  
             }else{  
                 var url =  'importBox';  
                 var formData = new FormData($("#batchUpload")[0]);  
                 user.sendAjaxRequest(url,'POST',formData);  
             } 
         }); 
          
    };  
  
    this.sendAjaxRequest = function(url,type,data){  
        $.ajax({  
            url : url,  
            type : type,  
            data : data,  
            success : function(result) {  
                alert( result);  
            },  
            error : function() {  
                alert( "Excel upload failure!");  
            },  
            cache : false,  
            contentType : false,  
            processData : false  
        });  
    };  
}  
  

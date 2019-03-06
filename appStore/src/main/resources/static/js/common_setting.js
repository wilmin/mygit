$(document).ready(function () {
    $("#header").load("header.html");
    $("#sidebar_setting").load("sidebar_setting.html", function () {
    	if(sessionStorage.getItem("sidebar")=="siderbar_setting") {
    		if (sessionStorage.getItem("active") == null) {
                $("#setting_user").addClass("active");
                $("#setting_user").parent().parent().parent().addClass("in");
            } else {
                $("#" + sessionStorage.getItem("active")).addClass("active");
                $("#" + sessionStorage.getItem("active")).parent().parent().parent().addClass("in");
            }
    		
    	} else {
    		sessionStorage.setItem("sidebar", "siderbar_setting");
    		$('#header_setting').addClass="active";
    		$('#header_device').removeClass="active";
    		$("#setting_user").addClass("active");
            $("#setting_user").parent().parent().parent().addClass("in");
    		
    	}
//        if (sessionStorage.getItem("authority") == "2") {
//            $("#userManagerment").remove();
//        }
        $("#sidebar_setting li").click(function () {
            sessionStorage.setItem("active", $(this).attr("id"));
            if ($(this).attr("id") == "logout") {
                sessionStorage.removeItem("active");
            }
        });
        $("#logout").click(function () {
            alert("Logout!");
            $.ajax({
                type: "get",
                url: "logout",
                success: function (result) {
                    console.log(result);
                    result = $.parseJSON(result);
                    if (result.code == 1) {
                        location.href = "login";
                        localStorage.clear();
                        sessionStorage.clear();
                    } else {
                        alert("error!");
                    }
                },
                error: function (result, xhr) {
                    console.log(result);
                    console.log(xhr);
                }
            });
        });
    });
});
function header_settingClick() {
	$('#header_setting').addClass=("active");
	$("#setting_user").addClass("active");
	sessionStorage.setItem("active", "setting_user");
    $("#setting_user").parent().parent().parent().addClass("in");
}
$('input[type="checkbox"]').click(function () {
    if ($(this).context.checked == false) {
        $("#" + $(this).val()).addClass("displayNone");
        //取消DVB单选框的框选
        if ($("#" + $(this).val()).children().find("input").attr("name") == "DVBSupport") {
            $("#yesSupport").prop("checked", false);
            $("#noSupport").prop("checked", false);
        } else {//如果不是单选框，就删除value值
            $("#" + $(this).val()).children().find("input").val("");
        }
    } else {
        $("#" + $(this).val()).removeClass("displayNone");
    }
});

//设置模态框,不能通过点击背景让模态框消失
$('.modal').modal({
    backdrop: "static",
    show: false,
});

//截取文件名
function getFileName(filePath) {
    var index = filePath.lastIndexOf('\\') + 1;
    var lastIndex = filePath.lastIndexOf('.');
    if (lastIndex == -1) {
        lastIndex = filePath.length - 1;
    }
    var filename = filePath.substring(index, lastIndex);
    return filename;
}

//$('#collapseHome').on('shown.bs.collapse', function () {
//	alert("shown");
//    sessionStorage.setItem("collapseHomeStatus", "shown");
//})
//
//$('#collapseHome').on('hidden.bs.collapse', function () {
//	alert("hidden");
//    // 执行一些动作...
//    sessionStorage.setItem("collapseHomeStatus", "hidden");
//})
//
//$('#collapseDevice').on('shown.bs.collapse', function () {
//    // 执行一些动作...
//	alert("shown");
//    sessionStorage.setItem("collapseDeviceStatus", "shown");
//})
//
//$('#collapseDevice').on('hidden.bs.collapse', function () {
//    // 执行一些动作...
//	alert("hidden");
//    sessionStorage.setItem("collapseDeviceStatus", "hidden");
//})
//
//$('#collapseUpgrade').on('shown.bs.collapse', function () {
//    // 执行一些动作...
//    sessionStorage.setItem("collapseUpgradeStatus", "shown");
//})
//
//$('#collapseUpgrade').on('hidden.bs.collapse', function () {
//    // 执行一些动作...
//    sessionStorage.setItem("collapseUpgradeStatus", "hidden");
//})
//
//$('#collapseOperations').on('shown.bs.collapse', function () {
//    // 执行一些动作...
//    sessionStorage.setItem("collapseOperationsStatus", "shown");
//})
//
//$('#collapseOperations').on('hidden.bs.collapse', function () {
//    // 执行一些动作...
//    sessionStorage.setItem("collapseOperationsStatus", "hidden");
//})
//
//$('#collapseLogs').on('shown.bs.collapse', function () {
//    // 执行一些动作...
//    sessionStorage.setItem("collapseLogsStatus", "shown");
//})
//
//$('#collapseLogs').on('hidden.bs.collapse', function () {
//    // 执行一些动作...
//    sessionStorage.setItem("collapseLogsStatus", "hidden");
//})

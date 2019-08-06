$(function(){
	$(".tit_hov").slide({ titCell:".hd li",mainCell:".bd" });
	
})
function login(){
		$('.ixm_tcc_bg,.ixm_dl_tcc').slideDown()
	}
	$('.ixm_dl_tu').click(function(){
		$('.ixm_ewm_con').slideDown();
		$('.ixm_dl_con').slideUp(0);
	})
	$('.ixm_dl_tu1').click(function(){
		$('.ixm_ewm_con').slideUp(0);
		$('.ixm_dl_con').slideDown();
	})
	$('.ixm_dl_tcc .close').click(function(){
		$(this).parent('.ixm_dl_tcc').slideUp()
		$('.ixm_tcc_bg').slideUp()
	})
	
	var changeText = $("#changeText").text();
	if(changeText=="手机动态码登录"){
		$("input[name='loginType']").val("0");
	}else{
		$("input[name='loginType']").val("3");
	}
	function changeLoginType(obj){
		var loginType = $("input[name='loginType']").val();
		var targetObj = $(obj);
		if(loginType=="0"){
			$("#passDiv").attr("style","display:none;");
			$("#smsDiv").attr("style","");
			$("#passDiv1").attr("style","display:none;");
			$("#smsDiv1").attr("style","");
			targetObj.text("密码登录");
			$("input[name='loginType']").val("3");
	
		}else if(loginType=="3"){
			$("#passDiv").attr("style","");
			$("#smsDiv").attr("style","display:none;");
			$("#passDiv1").attr("style","");
			$("#smsDiv1").attr("style","display:none;");		
			targetObj.text("手机动态码登录");
			$("input[name='loginType']").val("0");
		}
	
	}
/**
 * 注册
 * @author hlijing@linewell.com
 * @since 2018-05-01
 */
;
define(function(require, exports, module) {
    var md5 = require("common/js/utils/md5");
    var appAjax  = require("app-ajax");
    var cookie = require("common/js/utils/cookie");
    var verifyCode = require("common/js/common/verify-code");
    var notify = require("ai/js/utils/notify");
    
    var slideVerify = require("common/js/utils/sliding-verify");
    
    require("common/js/jquery/ite-ie9");
	require("common/js/utils/validationEngine-zh_IC");
	require("common/js/jquery/jquery.validationEngine");
	
	var emailRule = /^[0-9a-z_][_.0-9a-z-]{0,31}@([0-9a-z][0-9a-z-]{0,30}\.){1,4}[a-z]{2,4}$/;
	var phoneRule = /(^1[3456789]\d{9}$)|^$/;
	
	// 滑动验证成功及发送验证码票据
	var slideTrue = false;
	var sendTicket = "";
	
	var slide;

    /**
     * 远程数据操作
     */
    var remoteDataOperation = {
    	
    	/**
    	 * 注册
    	 * @param {Object} postData
    	 * @param {Object} callback
    	 */
    	registerAction : function(postData, callback) {
    		
    		appAjax.loadingButton($("#registerBtn")).postJson({
				service : "USER_REGISTER",
				data : postData,
				success : function(row, d) {
					
					// 回调成功把发送验证码按钮亮起来
					$("#senCodeBtn").removeClass("disabled smsTime").html("获取验证码");
        			countdownnumflag = 0;
        			
					if(row){
						callback && callback(row);
					}
				},
				error : function(row, d) {        			
					notify.showTip(d.message);
				}
			});
    	},
    		
        /**
         * 发送手机验证码
         * @param {Object} postData
         * @param {Object} $btn
         */
		fetchVerifyCode : function(postData, $btn) {
			if ($btn.attr("disabled") && !slideTrue) {
				return;
			}
			
			// 如果号码为空，则要后台根据当前的用户去获取号码
			verifyCode.fetchVerifyCode($btn, postData, "USER_GET_CODE", function(data) {
				(countdownnumflag != -1) && notify.showTip("验证码已发送，请注意查收。", "success");
			}, true, function(msg) {
				slideTrue = false;
				local.hideSlideSuccess();
				slide.refresh();
				slide.options.isShowImg = true;
				notify.showTip(msg);
			});
		},
		
		/**
		 * 滑动验证码验证结果
		 * @param {Object} postData
		 * @param {Object} callback
		 */
		getSendTips : function(postData, callback, failCallback) {
			
			appAjax.postJson({
				service : "GET_IMAGE_VERIFY_RESULT",
				data : postData,
				success : function(row, d) {
					callback && callback(row);
				},
				error : function(msg) {
					failCallback && failCallback(msg);
				}
			});
		}
		
    };
    
    /**
     * 本地操作
     */
    var local = {
    	
    	/**
    	 * 隐藏验证成功
    	 */
    	hideSlideSuccess: function() {
    		$("#slideSuccess").addClass("hide");
    	},
    	
    	/**
    	 * 初始化滑动验证码
    	 */
    	initSlideVerify : function() {
    		
    		// 滑动验证框宽度
    		var slideWidth = $(".input-box").width();
    		var slideHeight = $(".input-box").height();
    		
    		slide = slideVerify.init($('#slideVpanel'), {
		    	type : 2, // 类型
		    	mode : "pop", // 弹出式
		    	imgSize : {
		        	width: slideWidth + "px",
		        	height: Math.round(slideWidth/2) + "px",
		        },
		        blockSize : {
		        	width: Math.round(slideWidth/2)*0.26+4 + "px",
					height: Math.round(slideWidth/2) + "px"
		        },
		        barSize : {
		        	width: slideWidth + "px",
		        	height: slideHeight + "px"
		        },
		        success : function(d, callback) {
		        	
		        	// 账号未填写不进行验证
		        	var userphone = $("#userphone").val();
		        	if(!userphone || (!phoneRule.test(userphone) && !emailRule.test(userphone))) {
		        		local.hideSlideSuccess();
		        		slideTrue = false;
		        		callback && callback();
		        		$("#userphone").blur().focus();
		        		return;
		        	}
		        	
		        	// 获取发送验证码票据
		        	remoteDataOperation.getSendTips({
		        		ticket : d.options.ticket,
		        		dragPos : d.htmlDoms.left_bar[0].clientWidth,
		        		imageWidth : slideWidth
	 	        	}, function(ret) {
	 	        		
		        		if(ret && ret.verifyPass && ret.accessToken) {
		        			slideTrue = true;
		        			slide.options.isShowImg = false;
		        			
		        			// 验证成功
		        			sendTicket = ret.accessToken;
		        			$("#slideSuccess").removeClass("hide");
			        		$(".verify-move-block").css('background-color', '#5cb85c');
				        	$('.verify-left-bar').css({'border-color': '#5cb85c', 'background-color': '#d2f4ef'});
				        	$('.verify-icon').css('color', '#fff').removeClass('icon-right icon-close').addClass('icon-check');
		        		}
		        		
		        		callback && callback();
		        		
		        	}, function(msg) {
		        		
		        		// 验证错误
		        		slideTrue = false;
	        			sendTicket = "";
	        			local.hideSlideSuccess();
	        			$(".verify-move-block").css('background-color', '#d9534f');
		            	$('.verify-left-bar').css('border-color', '#d9534f');
		            	$('.verify-icon').css('color', '#fff').removeClass('icon-right icon-check').addClass('icon-close');
		            	callback && callback();
		        	});
		        },
		        error : function() {
		        	slideTrue = false;
		        	local.hideSlideSuccess();
		        }
		        
		    });
    	}
    };
    
    /**
     * 初始化数据
     */
    var _initData = function() {
    	
    	local.initSlideVerify();
    	
    	var second = 15;
    	setInterval(function(){
    		second--;
    		if(second>0){
    			$("#downDesc").html('（'+second+'s）继续');
    		}else{
    			$("#downDesc").html('继续');
    			$("#downDesc").removeClass('disabled');
    			return;
    		}
    	} ,1000);
    };

    /**
     * 初始化事件
     */
    var _initEvent = function() {
    	
		// 自动清除复制手机号中的前后空格
		$("#userphone").bind("blur", function() {
			$(this).attr("value", $(this).attr("value").replace(/(^\s*)|(\s*$)/g, ""));
		});
		
		$("#userphone").on("input", function(){
			if(!slideTrue) {
				return;
			}
			slideTrue = false;
			local.hideSlideSuccess();
			slide.refresh();
			slide.options.isShowImg = true;
		})
		
		// 发送手机验证码
		$("#senCodeBtn").on("click", function() {
			var $this = $(this);
			var userphone = $("#userphone").val();
			if($this.hasClass("disabled")) {
				return;
			}
			
			// 验证手机号/邮箱格式
			if(!$.trim(userphone) || (!phoneRule.test(userphone) && !emailRule.test(userphone))) {
				$("#userphone").blur().focus();
				return;
			}
			
			// 验证滑动验证码
			if(slideTrue && sendTicket) {
				
				var postData = {
					competeId : INNO_CHINA_CONSTANTS.COMPETEID,
					accessToken : sendTicket
				}
				
				if(emailRule.test(userphone)){
					postData.email = userphone;
					postData.registerType = 2;
				}else{
					postData.phone = userphone;
					postData.registerType = 1;
				}
				
				remoteDataOperation.fetchVerifyCode(postData, $("#senCodeBtn"));
			}else {
				notify.showTip("请拖动图形验证码进行验证");
			}

		});
    	
    	$("#userphone").addRule("checkUserNameOnly", {
			"alertText" : "此手机号码已被注册",
			"func" : function (field, rules, i, options, customErrorMessage) {
				var that = $(field);
				var username = that.val();
				var flag = false;
				appAjax.postJson({
					service : "VALIDATE_PHONE",
					isAsync : false,
					data : {
						phone : username
					},
					success : function(row, d) {
						
						// content为false是没注册过的用户
						if(!row) {
							flag = true;
						}
					}
				});
				
				return flag;
			}
		});
        
    	$("#registerForm").validationEngine({
			maxErrorsPerField : 1,
			scroll : false,
			promptPosition : "bottomLeft",
			addPromptClass : "formError-noArrow",
			onSuccess : function(){
				
				var username = $("#userphone").val();
            	
            	var postData = {
            		competeId : INNO_CHINA_CONSTANTS.COMPETEID,
					password : md5.hex_md5($("#password").val()),
					verifyCode : $("#verifyCode").val()
				}
            	
            	// 判断账号是否是邮箱
            	if(emailRule.test(username)){
					postData.email = username;
					postData.registerType = 2;
				}else{
					postData.phone = username;
					postData.registerType = 1;
				}

				remoteDataOperation.registerAction(postData, function(ret) {
					
					// 注册成功跳转成功页
        			cookie.setCookie("token_ai", ret.userTokenId);
					location.href = INNO_CHINA_CONSTANTS.BASE_URL + "/login/success.html";
					
				});
			},
			onFieldSuccess: function(e) {
				
				// 去掉错误样式
				$(".input-box").removeClass("no-pass");
			},
			onFieldFailure: function(e) {
				
				// 字段错误时添加错误样式类
				$(arguments[0]).parent(".input-box").addClass("no-pass");
			}
		});
		
		// 注册操作
    	$("#registerBtn").click(function(event){
			$("#registerForm").validationEngine("validate");
			event.stopPropagation();
		});
		
		// 捕捉验证码、用户名、密码的回车事件		
		$("#password, #userphone, #telPhone").keyup(function(e) {
			if (13 === e.keyCode) { 
				$("#registerBtn").trigger("click");
			}
		});
		
		// 密码框显隐
		$("#eyeBtn").on("click", function() {
			var $eyeBtn = $("#eyeBtn");
			var $pass = $("#password");
			if($eyeBtn.hasClass('icon-eye-close')){
				$eyeBtn.removeClass('icon-eye-close');
				$eyeBtn.addClass('icon-eye-open');
				document.getElementById("password").setAttribute("type", "text");
			}else {
				$eyeBtn.removeClass('icon-eye-open');
				$eyeBtn.addClass('icon-eye-close');
				document.getElementById("password").setAttribute("type", "password");
			}
		});
		
		// 继续
		$("#downDesc").on("click", function(){
			if($(this).hasClass("disabled")){
				return;
			}else{
				if(!$("#agreeSignup").is(":checked")) {
					notify.showTip("您还未接受服务条款");
				}else{
					$("#proBox").addClass("hide");
				}
			}
		});
    };

    /**
     * 初始化方法，可初始化数据、视图、事件，
     * 数据、视图、事件三者之间没有绝对的先后顺序，视具体情况而定
     */
    var _init = function() {
    	
    	// 初始化数据
    	_initData();
    	
        // 初始化事件
        _initEvent();
    };


    $(function(){
        _init();
    });
});
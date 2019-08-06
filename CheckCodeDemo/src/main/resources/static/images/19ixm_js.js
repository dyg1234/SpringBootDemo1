$(function(){
	$(".tit_hov").slide({ titCell:".hd li",mainCell:".bd" });
	$(".n_tit_hov").slide({ titCell:".n_hd li",mainCell:".n_bd" });
	$('.bm_con').click(function(event){
		$('.bm_con1').slideDown();
		event.stopPropagation();

	})
	
	$(".inp").focus(function(){
		$(".ss_con1").slideDown();
	})
	$(".inp").blur(function(){
		$(".ss_con1").slideUp();
	})
	$(".pf_con").addClass("on111");
    
	$('.pf_b_con i.close').click(function(){
		$('.pf_b_con').slideUp()
	})
	
	$(".tit_bg1").slide({mainCell:".bd ul",autoPage:true,effect:"top",autoPlay:true,vis:1});
	$(".fullSlide").slide({ titCell:".hd ul", mainCell:".bd ul", effect:"fold", autoPlay:true, autoPage:true, trigger:"click" });
	$(".bmlq_con").slide({ mainCell:".bd", effect:"left", delayTime:800,vis:1,scroll:1,pnLoop:false,trigger:"click",easing:"easeOutCubic" });
	$(".jpyy_ty_pic").slide({ mainCell:".slides", effect:"fold", autoPlay:true, autoPage:true, trigger:"click" });
	
	function is_Menu(){ 
		var w = $(window).innerWidth(); 
		if(w<1000) {
			
			$(".fwdt_list1").slide({mainCell:"ul",autoPage:true,effect:"left",autoPlay:true,vis:3});
			TouchSlide({ slideCell:"#focusBox",titCell:".num", mainCell:".pic", effect:"leftLoop", autoPage:true,autoPlay:true,delayTime:600,easing:"easeOutCubic"});
			$('.tcc_bg,.tcc_con').hide();
			$('.menu_icon').click(function(event){
				$('.head_bg').css({"z-index":"-1"})
				$(this).hide();
				$('.menu_bg').animate({'right':'0'},500);
				event.stopPropagation();
				
			})
			
			$(document).click(function(){
				$('.menu_bg').animate({'right':'-100%'},500);
				$('head_bg').css({"z-index":4})
				$('.menu_icon').show();
			})
		}else{
			
			$(".jpyy_con").slide({ mainCell:".bd", effect:"left", delayTime:800,vis:1,scroll:1,pnLoop:false,trigger:"click",easing:"easeOutCubic" });
			$(".con1").slide({mainCell:"ul",autoPage:true,effect:"left",autoPlay:true,vis:4});
			$(".fwdt_list1").slide({mainCell:"ul",autoPage:true,effect:"left",autoPlay:true,vis:6});
			$(".focusBox").slide({ titCell:".num li", mainCell:".pic",effect:"fold", autoPlay:true,trigger:"click",startFun:function(i){		 
				$(".focusBox .txt li").eq(i).animate({"bottom":0}).siblings().animate({"bottom":-50});	
			}});
		}
	}
	is_Menu()
	
	$('.wycy').click(function(){
		$('.qtms_r_con').slideDown();
	})
	function a_more(selector){
		$(selector).hover(function(){
			var str = $(this).attr("href");
			$(this).parent().parent().next().children().attr("href",str);
		});
	}
	a_more(".a_more li a");
	
	var timer  = null;
	$('.top').click(function(){
		cancelAnimationFrame(timer);
		timer = requestAnimationFrame(function fn(){
			var oTop = document.body.scrollTop || document.documentElement.scrollTop;
			if(oTop > 0){
				//document.body.scrollTop = document.documentElement.scrollTop = oTop - 50;
				scrollTo(0,oTop-50);
				//scrollBy(0,-50);
				timer = requestAnimationFrame(fn);
			}else{
				cancelAnimationFrame(timer);
			}    
		});
     })
	
	$(document).click(function(){
        $(".bm_con1").slideUp()
    })
	$('.gl_tit1').click(function(){
		$('.gl_list').slideToggle();
	})
	$('.fwdt_l li').hover(function(){
		$(this).addClass('on').siblings().removeClass('on');
	},function(){
		$(this).removeClass('on');
	})
	
	
	var yyzx_h = $('.yyzx_con6').height();
		//console.log($('.yyzx_con6').height())
	$(".zk_btn").on("click",function(){
		if(!$(this).hasClass("down"))
		{
			$(this).addClass("down");
			$(this).parent('.yyzx_con5').animate({"height":yyzx_h},500);
			$(this).text('收起');
		}
		else{
			$(this).removeClass("down");
			$(this).parent('.yyzx_con5').animate({"height":"144px"},500);
			$(this).text("展开");
		}
	});
	if($("#picScroll-left1").length >0){
        $("#picScroll-left1").slide({mainCell:".picList",prevCell:".pic_prev",nextCell:".pic_next",autoPage:true,effect:"left",autoPlay:true,vis:3});
	}
    if($("#picScroll-left0").length >0){
        $("#picScroll-left0").slide({mainCell:".picList",prevCell:".pic_prev",nextCell:".pic_next",autoPage:true,effect:"left",autoPlay:true,vis:3});
    }
    if($("#picScroll-left2").length >0){
        $("#picScroll-left2").slide({mainCell:".picList",prevCell:".pic_prev",nextCell:".pic_next",autoPage:true,effect:"left",autoPlay:true,vis:3});
    }

})

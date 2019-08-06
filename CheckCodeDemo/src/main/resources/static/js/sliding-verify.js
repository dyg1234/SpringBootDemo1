/*
;define(function(require, exports, module) {
	var appAjax  = require("app-ajax");

	var remote = {
		
		/!**
		 * 获取滑动验证背景图
		 * @param {Object} service
		 * @param {Object} callback
		 *!/
		getImgName : function(service, callback) {
			
			appAjax.postJson({
				service : service,
				success : function(row, d) {
					if(row) {
						callback && callback(row);
					}
				},
				error : function(row, d) {
					
				}
			});
		}
	};
	
    //定义Slide的构造函数
    var Slide = function(ele, opt) {
    	
        this.$element = ele,
        this.defaults = {
        	getBgService : "GET_IMAGE_VERIFY_INFO",
        	type : 1, // 普通滑动， 2为拖拉拼图滑动
        	mode : 'fixed',	// 弹出式pop，固定fixed
        	yRate : 0, // 竖轴值
        	ticket : "",
        	vOffset : 5,	// 误差量，根据需求自行调整
	        vSpace : 5,	// 间隔
            explain : '向右滑动完成验证',
            imgUrl : INNO_CHINA_CONSTANTS.RESOURCE_URL,
			imgName : '',
			smallImg : '',
			isShowImg : true,
            imgSize : {
	        	width: '400px',
	        	height: '200px',
	        },
	        blockSize : {
	        	width: '50px',
	        	height: '50px',
	        },
            circleRadius: '0px',
	        barSize : {
	        	width : '400px',
	        	height : '37px',
	        },
            ready : function(){},
        	success : function(){},
            error : function(){}
        },
        this.options = $.extend({}, this.defaults, opt)
    };
    
    
    //定义Slide的方法
    Slide.prototype = {
        
        init: function() {
        	var _this = this;
        	
        	// 获取滑动验证码背景图
    		remote.getImgName(_this.options.getBgService, function(ret) {
    			
    			// 滑动验证码背景图赋值
    			ret.verifyBigImage && (_this.options.imgName = ret.verifyBigImage);
    			ret.verifySmallImage && (_this.options.smallImg = ret.verifySmallImage);
    			ret.ticket && (_this.options.ticket = ret.ticket);
//  			ret.yRate && (_this.options.yRate = ret.yRate);

    			// 加载页面
	        	_this.loadDom();
	    		_this.refresh(true);

	    		_this.options.ready();

	        	_this.$element[0].onselectstart = document.body.ondrag = function(){
					return false;
				};

	        	if(_this.options.mode == 'pop')	{
	        		_this.$element.on('mouseover', function(e){
	        			_this.options.isShowImg && _this.showImg();
		        	});

		        	_this.$element.on('mouseout', function(e){
		        		_this.hideImg();
		        	});


		        	_this.htmlDoms.out_panel.on('mouseover', function(e){
		        		_this.options.isShowImg && _this.showImg();
		        	});

		        	_this.htmlDoms.out_panel.on('mouseout', function(e){
		        		_this.hideImg();
		        	});
	        	}

	        	//按下
	        	_this.htmlDoms.move_block.on('touchstart', function(e) {
	        		_this.options.isShowImg && _this.start(e);
	        	});

	        	_this.htmlDoms.move_block.on('mousedown', function(e) {
	        		_this.options.isShowImg && _this.start(e);
	        	});

	        	//拖动
	            window.addEventListener("touchmove", function(e) {
//	            	e.preventDefault();
	            	_this.move(e);
	            });

	            // 禁用整页面拖动
	        	$(".user-input-box").on("touchmove",function(e){
	        		e.preventDefault();
	        	});

	            window.addEventListener("mousemove", function(e) {
//					e.preventDefault();
	            	_this.move(e);
	            });

	            //鼠标松开
	            window.addEventListener("touchend", function() {
	            	_this.end();
	            });
	            window.addEventListener("mouseup", function() {
	            	_this.end();
	            });

	            // 刷新按钮
	            _this.$element.find('.verify-refresh').on('click', function() {
					_this.refresh();
	            });
	    	});


        },

        //初始化加载
        loadDom : function() {

        	this.img_rand = Math.floor(Math.random() * this.options.imgName.length);			//随机的背景图片
			this.status = false;	// 鼠标状态
        	this.isEnd = false;		// 是否验证完成
        	this.setSize = this.resetSize(this);	// 重新设置宽度高度
			this.plusWidth = 0;
			this.plusHeight = 0;
            this.x = 0;
            this.y = 0;
        	var panelHtml = '';
        	var tmpHtml = '';
			this.lengthPercent = (parseInt(this.setSize.img_width)-parseInt(this.setSize.block_width)- parseInt(this.setSize.circle_radius) - parseInt(this.setSize.circle_radius) * 0.8)/(parseInt(this.setSize.img_width)-parseInt(this.setSize.bar_height));

        	if(this.options.type != 1) {		// 图片滑动

//				panelHtml += '<div class="verify-img-out"><div class="verify-img-panel"><div class="verify-refresh" style="z-index:3"><i class="iconfont icon-refresh"></i></div><canvas class="verify-img-canvas" width="'+this.setSize.img_width+'" height="'+this.setSize.img_height+'"></canvas></div></div>';
				panelHtml += '<div class="verify-img-out ani-ini"><div class="verify-img-panel"><div class="verify-refresh" style="z-index:3"><i class="iconfont icon-refresh"></i></div><img class="verify-img-canvas" src="'+ this.options.imgName +'" width="'+this.setSize.img_width+'" height="'+this.setSize.img_height+'"/></div></div>';
				this.plusWidth = parseInt(this.setSize.block_width) + parseInt(this.setSize.circle_radius) * 2 - parseInt(this.setSize.circle_radius) * 0.2;
				this.plusHeight = parseInt(this.setSize.block_height) + parseInt(this.setSize.circle_radius) * 2 - parseInt(this.setSize.circle_radius) * 0.2;

//				tmpHtml = '<canvas class="verify-sub-block"  width="'+this.plusWidth+'" height="'+this.plusHeight+'" style="left:0; position:absolute;" ></canvas>';
				tmpHtml = '<img class="verify-sub-block" src="' + this.options.smallImg + '" width="'+this.plusWidth+'" height="'+this.plusHeight+'" style="left:0; position:absolute;"/>';
        	}

			panelHtml += tmpHtml+'<div class="verify-bar-area"><span  class="verify-msg">'+this.options.explain+'</span><div class="verify-left-bar"><span  class="verify-msg"></span><div  class="verify-move-block"><i  class="verify-icon iconfont icon-right"></i></div></div></div>';
        	this.$element.append(panelHtml);

        	this.htmlDoms = {
        		sub_block : this.$element.find('.verify-sub-block'),
        		out_panel : this.$element.find('.verify-img-out'),
        		img_panel : this.$element.find('.verify-img-panel'),
				img_canvas : this.$element.find('.verify-img-canvas'),
        		bar_area : this.$element.find('.verify-bar-area'),
        		move_block : this.$element.find('.verify-move-block'),
        		left_bar : this.$element.find('.verify-left-bar'),
        		msg : this.$element.find('.verify-msg'),
        		icon : this.$element.find('.verify-icon'),
        		refresh :this.$element.find('.verify-refresh')
        	};


        	this.$element.css('position', 'relative');
        	if(this.options.mode == 'pop') {
        		this.htmlDoms.out_panel.addClass('ani-hide')
        		this.htmlDoms.out_panel.css({ 'position': 'absolute', 'bottom': this.options.barSize.height});
        		this.htmlDoms.sub_block.css({'display': 'none'});
        	}else {
        		this.htmlDoms.out_panel.css({'position': 'relative'});
        	}

			this.htmlDoms.out_panel.css('height', parseInt(this.setSize.img_height) + this.options.vSpace + 'px');
			this.htmlDoms.img_panel.css({'width': this.setSize.img_width, 'height': this.setSize.img_height});
			this.htmlDoms.bar_area.css({'width': this.setSize.bar_width, 'height': Number(this.setSize.bar_height.replace("px", "")) - 1 + "px", 'line-height':this.setSize.bar_height});
        	this.htmlDoms.move_block.css({'width': this.setSize.bar_height, 'height': this.setSize.bar_height});
        	this.htmlDoms.left_bar.css({'width': this.setSize.bar_height, 'height': this.setSize.bar_height});

        	this.randSet();
        },

		drawImg: function(obj, img) {

	       	var canvas = this.htmlDoms.img_canvas[0];
			if(canvas) {
				var ctx=canvas.getContext("2d");
				ctx.drawImage(img,0,0, parseInt(this.setSize.img_width), parseInt(this.setSize.img_height));

				graphParameter = {
					x : this.x,
					y : this.y,
					r : parseInt(this.setSize.circle_radius),
					w : (parseInt(this.setSize.block_width) - 2 * parseInt(this.setSize.circle_radius)) / 2,
					h :	(parseInt(this.setSize.block_height) - 2 * parseInt(this.setSize.circle_radius)) / 2
				};

				obj.drawRule(ctx, graphParameter);
			}

			var canvas2 = this.htmlDoms.sub_block[0];
			if(canvas2) {
				var proportionX = img.width/parseInt(this.setSize.img_width);
				var proportionY = img.height/parseInt(this.setSize.img_height);

				var ctx2=canvas2.getContext("2d");

				ctx2.restore();
				ctx2.drawImage(img,this.x * proportionX, (this.y - parseInt(this.setSize.circle_radius) - parseInt(this.setSize.circle_radius) * 0.8) * proportionY,this.plusWidth * proportionX,this.plusHeight * proportionY,0,0,this.plusWidth,this.plusHeight);
				ctx2.save();
				ctx2.globalCompositeOperation = "destination-atop";

				graphParameter.x = 0;
				graphParameter.y = parseInt(this.setSize.circle_radius) + parseInt(this.setSize.circle_radius) * 0.8;
				obj.drawRule(ctx2, graphParameter);
			}
		},

		drawRule:function(ctx, graphParameter) {
			var x = graphParameter.x;
            var y = graphParameter.y;
            var r = graphParameter.r
            var w = graphParameter.w
            var h = graphParameter.h

            ctx.beginPath();
			ctx.moveTo(x, y);
			ctx.lineTo((x + w) + r * 0.4, y);
			ctx.arc((x + w) + r, y - r * 0.8, r, 0.7 * Math.PI, 0.3 * Math.PI);
			ctx.lineTo((x + (2 * w) + (2 * r)), y);
			ctx.lineTo((x + (2 * w) + (2 * r)), y + h);
			ctx.arc((x + (2 * w) + (2 * r)) + (r * 0.8), y + h + r, r, 1.2*Math.PI, 0.8*Math.PI);
			ctx.lineTo((x + (2 * w) + (2 * r)), y + (2 * h) + (2 * r));
			ctx.lineTo(x, y + (2 * h) + (2 * r));
			ctx.lineTo(x, y + h + 2 * r - r * 0.4);
			ctx.arc(x + (r * 0.8), y + h + r, r, 0.8 * Math.PI, 1.2 * Math.PI, true);
			ctx.lineTo(x, y);

            ctx.fillStyle="#fff";
			ctx.fill();
			ctx.closePath();
		},


        //鼠标按下
        start: function(e) {
        	if(this.isEnd == false) {
	        	this.htmlDoms.msg.text('');
	        	this.htmlDoms.move_block.css('background-color', '#0377ff');
	        	this.htmlDoms.left_bar.css('border-color', '#0377ff');
	        	this.htmlDoms.icon.css('color', '#fff');
	        	e.stopPropagation();
	        	this.status = true;
        	}
        },

        //鼠标移动
        move: function(e) {

        	if(this.status && this.isEnd == false) {
				if(this.options.mode == 'pop')	{
        			this.showImg();
				}

	            if(!e.touches) {    //兼容移动端
	                var x = e.clientX;
	            }else {     //兼容PC端
	                var x = e.touches[0].pageX;
	            }
	            var bar_area_left = Slide.prototype.getLeft(this.htmlDoms.bar_area[0]);
                var move_block_left = (x - bar_area_left); //小方块相对于父元素的left值

                // 高分屏判断添加
				if(this.getRatio() == 150){
					move_block_left = (x - bar_area_left)/0.75;
				}

	            if(this.options.type != 1) {		//图片滑动
					if(move_block_left >= (this.htmlDoms.bar_area[0].offsetWidth - parseInt(this.setSize.bar_height) + parseInt(parseInt(this.setSize.block_width)/2) - 2) ) {
	                	move_block_left = (this.htmlDoms.bar_area[0].offsetWidth - parseInt(this.setSize.bar_height) + parseInt(parseInt(this.setSize.block_width)/2)- 2);
	            	}
	            }else {		//普通滑动
	            	if(move_block_left >= this.htmlDoms.bar_area[0].offsetWidth - parseInt(parseInt(this.setSize.bar_height)/2) + 3) {
	            		this.$element.find('.verify-msg:eq(1)').text('松开验证');
	                	move_block_left = this.htmlDoms.bar_area[0].offsetWidth - parseInt(parseInt(this.setSize.bar_height)/2) + 3;
	            	}else {
	            		this.$element.find('.verify-msg:eq(1)').text('');
	            	}
	            }

	            if(move_block_left <= parseInt(parseInt(this.setSize.block_width)/2)) {
            		move_block_left = parseInt(parseInt(this.setSize.block_width)/2);
            	}


	            //拖动后小方块的left值
	            this.htmlDoms.move_block.css('left', move_block_left-parseInt(parseInt(this.setSize.block_width)/2) + "px");
	            this.htmlDoms.left_bar.css('width', move_block_left-parseInt(parseInt(this.setSize.block_width)/2) + "px");
				this.htmlDoms.sub_block.css('left', (move_block_left-parseInt(parseInt(this.setSize.block_width)/2)) * this.lengthPercent + "px");

	        }
        },

        //鼠标松开
        end: function() {

        	var _this = this;

        	//判断是否重合
        	if(this.status  && this.isEnd == false) {

        		if(this.options.type != 1) {		// 图片滑动

        			this.options.success(this, function() {
	        			_this.hideImg();
	    				if(!_this.htmlDoms.icon.hasClass("icon-check")){
	        				_this.refresh();
	        			}
        			});

//      			var vOffset = parseInt(this.options.vOffset);
//
//		            if(parseInt(this.x) >= (parseInt(this.htmlDoms.sub_block.css('left')) - vOffset) && parseInt(this.x) <= (parseInt(this.htmlDoms.sub_block.css('left')) + vOffset)) {
//		            	this.htmlDoms.move_block.css('background-color', '#5cb85c');
//		            	this.htmlDoms.left_bar.css({'border-color': '#5cb85c', 'background-color': '#fff'});
//		            	this.htmlDoms.icon.css('color', '#fff');
//		            	this.htmlDoms.icon.removeClass('icon-right');
//		            	this.htmlDoms.icon.addClass('icon-check');
//		            	this.htmlDoms.refresh.hide();
//		            	this.isEnd = true;
//		            	this.options.success(this);
//		            }else{
//		            	this.htmlDoms.move_block.css('background-color', '#d9534f');
//		            	this.htmlDoms.left_bar.css('border-color', '#d9534f');
//		            	this.htmlDoms.icon.css('color', '#fff');
//		            	this.htmlDoms.icon.removeClass('icon-right');
//		            	this.htmlDoms.icon.addClass('icon-close');
//
//
//		            	setTimeout(function () {
//					    	_this.refresh();
//					    }, 400);
//
//		            	this.options.error(this);
//		            }

        		}else {		//普通滑动

        			if(parseInt(this.htmlDoms.move_block.css('left')) >= (parseInt(this.setSize.bar_width) - parseInt(this.setSize.bar_height) - parseInt(this.options.vOffset))) {
        				this.htmlDoms.move_block.css('background-color', '#5cb85c');
        				this.htmlDoms.left_bar.css({'color': '#4cae4c', 'border-color': '#5cb85c', 'background-color': '#fff' });
        				this.htmlDoms.icon.css('color', '#fff');
		            	this.htmlDoms.icon.removeClass('icon-right');
		            	this.htmlDoms.icon.addClass('icon-check');
		            	this.htmlDoms.refresh.hide();
        				this.$element.find('.verify-msg:eq(1)').text('验证成功');
        				this.isEnd = true;
        				this.options.success(this);
        			}else {
        				this.$element.find('.verify-msg:eq(1)').text('');
        				this.htmlDoms.move_block.css('background-color', '#d9534f');
		            	this.htmlDoms.left_bar.css('border-color', '#d9534f');
		            	this.htmlDoms.icon.css('color', '#fff');
		            	this.htmlDoms.icon.removeClass('icon-right');
		            	this.htmlDoms.icon.addClass('icon-close');
		            	this.isEnd = true;

		            	setTimeout(function () {
		            		_this.$element.find('.verify-msg:eq(1)').text('');
					    	_this.refresh();
					    	_this.isEnd = false;
					    }, 400);

		            	this.options.error(this);
        			}
        		}

	            this.status = false;
        	}
        },

        //弹出式
        showImg : function() {
        	this.htmlDoms.out_panel.removeClass('ani-hide');
//      	this.htmlDoms.out_panel.css({'display': 'block'});
        	this.htmlDoms.sub_block.css({'display': 'block'});
        },

        //固定式
        hideImg : function() {
        	this.htmlDoms.out_panel.addClass('ani-hide');
//      	this.htmlDoms.out_panel.css({'display': 'none'});
        	this.htmlDoms.sub_block.css({'display': 'none'});
        },


        resetSize : function(obj) {
        	var img_width,img_height,bar_width,bar_height,block_width,block_height,circle_radius;	//图片的宽度、高度，移动条的宽度、高度
        	var parentWidth = obj.$element.parent().width() || $(window).width();
        	var parentHeight = obj.$element.parent().height() || $(window).height();

       		if(obj.options.imgSize.width.indexOf('%')!= -1){
        		img_width = parseInt(obj.options.imgSize.width)/100 * parentWidth + 'px';
		　　}else {
				img_width = obj.options.imgSize.width;
			}

			if(obj.options.imgSize.height.indexOf('%')!= -1){
        		img_height = parseInt(obj.options.imgSize.height)/100 * parentHeight + 'px';
		　　}else {
				img_height = obj.options.imgSize.height;
			}

			if(obj.options.barSize.width.indexOf('%')!= -1){
        		bar_width = parseInt(obj.options.barSize.width)/100 * parentWidth + 'px';
		　　}else {
				bar_width = obj.options.barSize.width;
			}

			if(obj.options.barSize.height.indexOf('%')!= -1){
        		bar_height = parseInt(obj.options.barSize.height)/100 * parentHeight + 'px';
		　　}else {
				bar_height = obj.options.barSize.height;
			}

			if(obj.options.blockSize) {
				if(obj.options.blockSize.width.indexOf('%')!= -1){
					block_width = parseInt(obj.options.blockSize.width)/100 * parentWidth + 'px';
			　　}else {
					block_width = obj.options.blockSize.width;
				}


				if(obj.options.blockSize.height.indexOf('%')!= -1){
					block_height = parseInt(obj.options.blockSize.height)/100 * parentHeight + 'px';
			　　}else {
					block_height = obj.options.blockSize.height;
				}
			}

			if(obj.options.circleRadius) {
				if(obj.options.circleRadius.indexOf('%')!= -1){
					circle_radius = parseInt(obj.options.circleRadius)/100 * parentHeight + 'px';
			　　}else {
					circle_radius = obj.options.circleRadius;
				}
			}

			return {img_width : img_width, img_height : img_height, bar_width : bar_width, bar_height : bar_height, block_width : block_width, block_height : block_height, circle_radius : circle_radius};
       	},

        //随机出生点位
        randSet: function() {
        	var rand1 = Math.floor(Math.random()*9+1);
        	var rand2 = Math.floor(Math.random()*9+1);
        	var top = rand1 * parseInt(this.setSize.img_height)/15 + parseInt(this.setSize.img_height) * 0.1;
        	var left = rand2 * parseInt(this.setSize.img_width)/15 + parseInt(this.setSize.img_width) * 0.1;

            this.x = left;
//          this.y = top;
			this.y = this.options.yRate*parseFloat(this.options.imgSize.height.replace("px", ""));

            if(this.options.mode == 'pop') {
                this.htmlDoms.sub_block.css({'top': '-'+(parseInt(this.setSize.img_height) + this.options.vSpace +  parseInt(this.setSize.circle_radius) + parseInt(this.setSize.circle_radius) * 0.8 - this.y - 2) +'px'});
            }else {
                this.htmlDoms.sub_block.css({'top': this.y - (parseInt(this.setSize.circle_radius) + parseInt(this.setSize.circle_radius) * 0.8) + 2 + 'px'});
            }


        },

        //刷新
        refresh: function(flag) {
			var _this = this;

			// 初始化时调用
			!flag && remote.getImgName(_thiverifyBigImages.options.getBgService, function(ret) {

    			// 滑动验证码背景图赋值
    			ret.verifyBigImage && (_this.options.imgName = ret.);
    			ret.verifySmallImage && (_this.options.smallImg = ret.verifySmallImage);
    			ret.ticket && (_this.options.ticket = ret.ticket);
    			ret.yRate && (_this.options.yRate = ret.yRate);
    			
    			_this.refreshDoms();
    			
    		});
    		
    		// 非初始化的时候重新请求数据
    		flag && _this.refreshDoms();
        },
        
        refreshDoms : function() {
        	var _this = this;
        	
        	// 重新渲染滑动验证码背景
        	$(".verify-img-canvas").attr("src", _this.options.imgName);
        	$(".verify-sub-block").attr("src", _this.options.smallImg);
        	
        	_this.htmlDoms.refresh.show();
        	_this.$element.find('.verify-msg:eq(1)').text('');
        	_this.$element.find('.verify-msg:eq(1)').css('color', '#000');
        	_this.htmlDoms.move_block.animate({'left':'0px'}, 'fast');
			_this.htmlDoms.left_bar.animate({'width': parseInt(_this.setSize.bar_height)}, 'fast');
			_this.htmlDoms.left_bar.css({'border-color': 'transparent'});
			
			_this.htmlDoms.move_block.css('background-color', '#fff');
			_this.htmlDoms.icon.css('color', '#1482ff');
			_this.htmlDoms.icon.removeClass('icon-close');
			_this.htmlDoms.icon.addClass('icon-right');
			_this.$element.find('.verify-msg:eq(0)').text(_this.options.explain);
        	
        	_this.randSet();
        	_this.img_rand = Math.floor(Math.random() * _this.options.imgName.length);			//随机的背景图片
	 
			var img = new Image();
//		    img.src = _this.options.imgUrl + _this.options.imgName[_this.img_rand];
	
			// base64转图片
			_this.options.imgName && (img.src = _this.options.imgName);
				
		 	// 加载完成开始绘制
//			 $(img).on('load', function(e) {
//	        	_this.drawImg(_this, this);
//	        });
				
			_this.isEnd = false;

			_this.htmlDoms.sub_block.css('left', "0px");
        },
        
        /!**
         * 获取当前像素密度
         *!/
        getRatio : function() {
        	var ratio = 0,
            screen = window.screen,
            ua = navigator.userAgent.toLowerCase();

	        if (window.devicePixelRatio !== undefined) {
	            ratio = window.devicePixelRatio;
	        }
	        else if (~ua.indexOf('msie')) {
	            if (screen.deviceXDPI && screen.logicalXDPI) {
	                ratio = screen.deviceXDPI / screen.logicalXDPI;
	            }
	        }
	        else if (window.outerWidth !== undefined && window.innerWidth !== undefined) {
	            ratio = window.outerWidth / window.innerWidth;
	        }
	
	        if (ratio) {
	            ratio = ratio*100;
	        }
			return ratio;
        },
        
        //获取left值
        getLeft: function(node) {
			var left = $(node).offset().left;
			if(this.getRatio() == 150) {
				left = $(node).offset().left*0.75;
			}
			
//            var left = 0;
//            var nowPos = node.offsetParent;
//
//            while(nowPos != null) {
//              left += $(nowPos).offset().left;
//              nowPos = nowPos.offsetParent;
//            }
            return left;
      }
    };
    
//  //在插件中使用slideVerify对象
//  $.fn.slideVerify = function() {
//      
//  };
    
    return {
    	
    	init : function($this, options, callbacks){
    		
    		var slide = new Slide($this, options);
        	slide.init();
        	
        	return slide;
    	}
    	
    }
});
*/

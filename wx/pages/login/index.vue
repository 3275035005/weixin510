<template>
    <view class="login_back">
        
        <!-- logo -->
        <view class="login_logo flex" style="font-size: 100rpx;color: #ffffff;">
           菜谱小程序登录
        </view>
        <!-- logo -->
        
        <!-- form -->
        <view class="login_form">
            <view class="item flex">
                <image src="/static/icon_name.svg" class="icon" mode="heightFix"></image>
                <input placeholder="请输入账号" type="text" v-model="username"> 
            </view>
            <view class="item flex" >
                <image src="/static/icon_password.svg" class="icon" mode="heightFix"></image>
                <input placeholder="请输入密码" type="password" v-model="password"/>
            </view>
            <view class="button">
                <button @click="submit">登录</button>
            </view>
			<view class="button1">
			    <button @click="zhuche">注册</button>
			</view>
        </view>
        <!-- form -->
        
        <!-- version -->
        <view class="login_version">
            v1.0
        </view>
        <!-- version -->
        
    
        
    </view>
</template>

<script>
	import { mapState, mapMutations } from "vuex"
	import {
	  userLogin
	} from "../api/api.js"
    export default {
        data() {
            return {
                username:'',
				password:''
            }
        },
        methods: {
            //   vuex储存信息
            ...mapMutations([
            	"setShopInfo", //设置店铺信息
            	"setShopPhone", //设置电话
            	"setShopStatus", //设置店铺状态
            	"initdishListMut", //设置购物车订单
            	"setStoreInfo",
            	"setBaseUserInfo", //设置用户基本信息
            	"setLodding",
            	"setToken", //设置token
            	"setDeliveryFee", //设置配送费
            ]),
            // 从vuex信息
            ...mapState([
            	"shopInfo", //店铺信息
            	"shopPhone", //电话
            	"orderListData",
            	"baseUserInfo", //用户信息
            	"lodding",
            	"token", //token
            	"deliveryFee", //配送费
            ]),
			
            // 提交
            submit(){
			
				
                let that = this
				if (this.username === "") {
				  return uni.showToast({
				    title: "账号不能为空",
				    duration: 1000,
				    icon: "none",
				  })
				} else if (this.password === "") {
				  return uni.showToast({
				    title: "密码不能为空",
				    duration: 1000,
				    icon: "none",
				  })
				}else{
					let _this = this;
					 userLogin({username:this.username, password:this.password})
						.then((res) => {
						if(res.code == 1){
						
							// 查询
							_this.setToken(res.data.token);
							// 保存配送费
							_this.setDeliveryFee(6)
							// 保存商铺信息
							_this.setShopInfo({
								shopName: '菜谱小程序',
								shopAddress: '北京'
							})
							uni.switchTab({
								url:'/pages/index/index'
							})
						}else{
							uni.showToast({
							  title: res.msg,
							  duration: 1000,
							  icon: "none",
							})
						}
							
						})
						.catch((err) => { 
							console.log(err)
						})
					
				}
                
            },
			zhuche(){
				uni.navigateTo({
					url: "/pages/register/index"
				});
			}
        }
    }
</script>

<style>
@import url(index.css);
</style>

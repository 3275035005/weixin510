<template>
    <view>
		<!-- 向上的图标-->
		<view class="side" @click="shareBub">
		  <view class="add">+</view>
		</view>

        <!-- tab -->
        <view class="page_tab flex five">
            <view @click="tab_change(index)" class="item" :class="current_tab == index?'active':''" v-for="(item,index) in tab" :key="index">{{item}}</view>
        </view>
        <!-- tab -->
        
        <!-- order -->
        <view class="order" v-if="current_tab == 0">
            
            <!-- item -->
            <view class="order_item page_box" v-for="(item,index) in shareList" :key="index">
                <view class="box flex"  @click="shareInfo(item.id)">
                    <image :src="item.image" class="goods_pic" mode="heightFix"></image>
                    <view class="goods_content">
                        <view class="goods_title">{{item.setmealName}}</view>
                        <view class="goods_price">{{item.employeeName}}</view>
                    </view>
                </view>
                <view class="bottom flex">
                    <view class="total">发布时间: <text>{{item.createTime}}</text></view>
                </view>
            </view>
        </view>
		 <view class="order" v-else>
			<view class="order_item page_box" v-for="(item,index) in shareList" :key="index">
			
				<view class="box flex" @click="shareInfo(item.id)">
					<image :src="item.image" class="goods_pic" mode="heightFix"></image>
					<view class="goods_content">
					<view class="goods_title">{{item.setmealName}}</view>
					<view class="goods_price">{{item.employeeName}}</view>
						<view class="goods_integral flex" v-if="item.status == '0'"><text class="s_01">审核中</text></view>
						<view class="goods_integral flex" v-if="item.status == '1'"><text class="s_02">通过</text></view>
						<view class="goods_integral flex" v-if="item.status == '2'"><text class="s_03">拒绝</text></view>
					</view>
				</view>
				<view class="bottom flex">
					<view class="total">发布时间: <text>{{item.createTime}}</text></view>
				</view>
			</view>
		</view>
    </view>
</template>

<script>
	import {
		shareList
	} from "../api/api.js"

    export default {
        data() {
            return {
                
                // current
                current_tab:0,

                // tab
                tab:['美食社区', '我的发布'],
				
                // list
                shareList:[]

            }
        },
        
        
		onLoad(e){
			this.getInit(0);
		},
		
        methods: {
           
			getInit(type){
				
				shareList(type).then(res=>{
					this.shareList =res.data
					console.log(res.data)
					console.log(this.shareList)
				})
			},
			
			// 跳转发布
			shareBub(){
				uni.navigateTo({
				  url: "/pages/share_sub/index"
				});
			},
            // tab
            tab_change(index){
                this.current_tab = index
				this.getInit(index);
            },
			
			shareInfo(id){
				uni.navigateTo({
				  url: "/pages/share_info/index?id="+id,
				});
			}
			
        },
        
    }
</script>

<style>
@import url(index.css);
</style>

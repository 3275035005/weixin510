<template>
	<view>

		<view class="pic_list">
			<view class="item flex page_box" @click="handleNewsClick(item)" v-for="(item,index) in list" :key="index">
				<image :src="item.image" class="item_pic" mode="aspectFill"></image>
				<view class="item_box">
					<view class="item_title" >{{item.name}}</view>
					<view class="item_info flex" style="margin-top: 50rpx;">
						<view class="info_author">{{item.categoryName}}</view>
						<view class="info_date">{{item.createTime}}</view>
					</view>
				</view>
			</view>
			<view class="page_nothing" v-if="list == ''">
				<image src="@/static/card_empty_1.svg" class="nothing_pic"></image>
				<view class="text">暂无更多收藏</view>
			</view>
		</view>

	</view>
</template>

<script>
	import {
		collectorList
	} from "../api/api.js"
	export default {
		onShow() {
			this.init();
		},
		data() {
			return {
				list: [],
			}
		},
		methods: {
			init() {
				let that = this;
				collectorList().then(res=>{
					this.list = res.data;
				})
			},
			
			// 跳转页面详情
			handleNewsClick(item) {
				uni.navigateTo({
					url:'/pages/setmealdish_info/index?params='+encodeURIComponent(JSON.stringify(item))
				})
			},
		}
	}
</script>

<style>
	@import url(index.css);
</style>
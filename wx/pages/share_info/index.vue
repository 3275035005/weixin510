<template>
	<view>

		<!-- show" -->
		<view class="video_show">
			<video :autoplay="autoplay" :src="share.video" :controls="controls"></video>
		</view>
		<!-- show" -->

		<!-- box -->
		<view class="video_box">

			<!-- info -->
			<view class="video_info page_box">
				<view class="info_top">
					<view class="info_title">{{share.setmealName}}</view>
					<view class="info_time">
						<span class="left"> 发布时间：{{share.createTime}} </span>
						<span class="right"> 发布人：{{share.employeeName}} </span>
					</view>
				</view>
				<view class="info_company flex">
					<view
						style="text-indent: 2em;line-height:2;width:100%;font-size: 28rpx;color: #999;">
						食材：{{share.dishName}}
						
					</view>
					<view
						style="text-indent: 2em;line-height:2;width:100%;font-size: 28rpx;color: #999;">
						制作步骤：{{share.working}}
						
					</view>
					<view
						style="text-indent: 2em;line-height:2;width:100%;font-size: 28rpx;color: #999;">
						心得体会：{{share.content}}
						
					</view>
				</view>
				
			</view>
			<!-- info -->
		</view>
		<!-- box -->

	</view>
</template>

<script>
	import {
		getShareById
	} from "../api/api.js"
	export default {
		data() {
			return {
				share: {
					
				},
				autoplay:false,
				controls:true,
				id:0
			}
		},
		onLoad(option) { //option为object类型，会序列化上个页面传递的参数
			this.id = option.id;
		
		},
		onShow() {
			this.getShare();
		},
		
		methods: {
			getShare() {
				getShareById(this.id).then(res=>{
					this.share = res.data
				})
			},


		}
	}
</script>

<style>
	@import url(index.css);
</style>
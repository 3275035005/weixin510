<template>
	<view>
		<!-- 基本信息 -->
		<view class="stock_info page_box">
			<!-- column -->
			<view class="page_column flex">
				<view class="line"></view>
				<view class="title">基本信息</view>
			</view>
			<!-- column -->

			<!-- list -->
			<view class="list">

				<!-- item -->
				<view class="item">
					<view class="item_title">菜谱名称<text>*</text></view>
					<view class="item_info">
						<input type="text" v-model="form.setmealName" placeholder="请填写菜谱名称" placeholder-class="placeholder"
							class="item_input">
					</view>
				</view>
				<!-- item -->
				<view class="item">
					<view class="item_title">食材<text>*</text></view>
					<view class="item_info">
						<input type="text" v-model="form.dishName" placeholder="请填写食材" placeholder-class="placeholder"
							class="item_input">
					</view>
				</view>
			</view>
			<!-- list -->

		</view>

		<view class="page_column flex">
			<view class="line"></view>
			<view class="title">制作步骤</view>
		</view>
		<view class="stock_notes page_box">
			<textarea v-model="form.working" placeholder="请填写制作步骤" placeholder-class="placeholder" />
		</view>
		<view class="page_column flex">
			<view class="line"></view>
			<view class="title">心得体会</view>
		</view>
		<view class="stock_notes page_box">
			<textarea v-model="form.content" placeholder="请填写心得体会" placeholder-class="placeholder" />
		</view>

		<view class="page_column flex">
			<view class="line"></view>
			<view class="title">分享图片</view>
		</view>
		<!-- column -->

		<view class="stock_voucher page_box">
			<u-upload :fileList="fileList1" @afterRead="afterRead1" @delete="deletePic1" name="1"
			 multiple :maxCount="1" ></u-upload>
		</view>
		<view class="page_column flex">
			<view class="line"></view>
			<view class="title">分享视频</view>
		</view>
		<!-- column -->
		
		<view class="stock_voucher page_box">
			<view class="add_picture">
			       <view class="many_photo" v-if="addVideo">
			            <image src="@/static/add.png" @click="btnVideo" style="height: 100rpx; width: 100rpx;"></image>
			        </view>
			        <view class="show_video"  v-if="showVideo">
			            <video :src="url" class="video"></video>
			           <image src="@/static/but_close.png" 
			                @click="DelImg" class="close-img"></image>
			        </view>
			</view>
		</view>
		<!-- 退出 -->
		<view class="page_button mini" @click="handleSubmit">提交</view>
		<!-- 退出 -->

	</view>
</template>

<script>
	import {
		insertShare
	} from "../api/api.js"
	export default {
		// 接受参数
		onLoad() { 
			
		},
		onShow() {
		},
		data() {
			return {
				showVideo: false,
				addVideo: true,
				form: {
					setmealName:'',
					dishName:'',
					working:'',
					content:'',
					image:'',
					video:''
				},
				url:'',
				// 宽度
				width: 350,
				// 高度
				height: 350,
				fileList1: [],
				imageList: [],
			}
		},
		methods: {
	
			handleSubmit(){
				console.log(this.form)
				insertShare(this.form).then(res=>{
					if (res.code === 1) {
						uni.showToast({
						  title: '分享成功',
						  duration: 1000,
						  icon: "success",
						});
						setTimeout(function() {
							uni.navigateBack({
									delta: 1
							})
						}, 2000)
					}
				})
			},

			// 配图 : 删除图片
			deletePic1(event) {
				this[`fileList${event.name}`].splice(event.index, 1)
				this.form.img = "";
			},

			// 新增图片
			async afterRead1(event) {
				let lists = [].concat(event.file)
				let fileListLen = this[`fileList${event.name}`].length
				lists.map((item) => {
					this[`fileList${event.name}`].push({
						...item,
						status: 'uploading',
						message: '上传中'
					})
				})
				for (let i = 0; i < lists.length; i++) {
					const result = await this.uploadFilePromise(lists[i].url)
					let item = this[`fileList${event.name}`][fileListLen]
					this[`fileList${event.name}`].splice(fileListLen, 1, Object.assign(item, {
						status: 'success',
						message: '',
						url: result
					}))
					fileListLen++
					this.form.image = result;
				}
			},

			uploadFilePromise(url) {
				return new Promise((resolve, reject) => {
					let that = this;
					let a = uni.uploadFile({
						url: 'http://127.0.0.1:8081/admin/common/upload', // 仅为示例，非真实的接口地址
						filePath: url,
						name: 'file',
						formData: {
							user: 'test'
						},
						success: (res) => {
							let resJson = JSON.parse(res.data);
							console.log(resJson)
							setTimeout(() => {
								resolve(resJson.data)
							}, 1000)
						}
					});
				})
			},
			
		// 点击上传视频
        btnVideo: function() {
            var self = this;
            uni.chooseVideo({
                count: 1,
                sourceType: ['camera', 'album'],
                success: function(res) {
					console.log(res)
                    self.showVideo = true
                    self.addVideo = false
                    self.url = res.tempFilePath;
                    uni.uploadFile({
                        // 需要上传的地址
                        url: 'http://127.0.0.1:8081/admin/common/upload', //接口地址
                        filePath: self.url,
                        name: 'file',
                        success: (res) => {
                        	let resJson = JSON.parse(res.data);
							console.log(resJson)
                        	setTimeout(() => {
                        		self.form.video = resJson.data
                        	}, 1000)
                        }
                    });
                }
            });
        },
		//右上角删除视频
        DelImg() {
            this.form.video = '',
			 this.url = '',
            this.showVideo = false,
            this.addVideo = true
         },

		},

	}
</script>

<style>
	@import url(index.css);
</style>
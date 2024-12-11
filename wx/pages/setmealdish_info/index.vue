<template>
    <view>
        
        <!-- swiper -->
        <view :indicator-dots="true" :autoplay="true" class="goods_swiper">
            <view class="swiper-item">
                <image :src="goods.image" mode="widthFix" class="swiper_img"></image>
            </view>
        </view>
        <!-- swiper -->
        
        <!-- info -->
        <view class="goods_info">
            <view class="info_title">{{goods.name}}</view>
            <view class="info_price">
                {{goods.price}}
            </view>
            <view class="info_attr flex">
                <view class="cell">辣度: {{goods.pungencyTypeName}} </view>
                <view class="cell">甜度: {{goods.sweetTypeName}} </view>
            </view>
        </view>
        <!-- info -->
        
        <!-- content -->
        <view class="goods_content">
			<u-parse style="width: 100%;" :content="description"></u-parse>
        </view>
        <!-- content -->
        
        <!-- footer -->
        <view class="goods_footer flex">
			<view class="left flex">
			    <view class="item">
					<button v-if="collectorId == null" @click="insertCollectorClick">
						<image src="/static/qxsc.svg" class="icon"></image>
						<view class="text">收藏</view>
					</button>
					<button v-else @click="deleteCollector">
						<image src="/static/sc.svg" class="icon" ></image>
						<view class="text">取消收藏</view>
					</button>
			    </view>
			</view>
            <view class="right">
                <button class="buy" @click="goods_buy">加入购物车</button>
            </view>
        </view>
        <!-- footer -->
        
        
    </view>
</template>
`
<script>
	import {
		querySetmealDishById,
		deleteCollectorById,
		insertCollector,
		newAddShoppingCartAdd
	} from "../api/api.js"
	
    export default {
        data() {
            return {
				description:'',
                // goods
                goods:{},
				dishMealData:[],
				collectorId: null, // 收藏id
            }
        },
		onLoad(option) {
			this.goods = JSON.parse(decodeURIComponent(option.params));
			this.description = this.goods.description
			console.log(this.description)
			this.getInit()
		},
		
        methods: {
            getInit(){
				console.log(this.goods)
				let that = this;
				querySetmealDishById(that.goods).then(res => {
					if (res.code === 1) {
						that.dishMealData = res.data.list;
						that.collectorId = res.data.collectorId;
					}
				})
			},
			
			// 加入收藏
			insertCollectorClick(){
				insertCollector({
					setmealId :this.goods.id
				}).then(res => {
					if (res.code === 1) {
						uni.showToast({
						  title: '收藏成功',
						  duration: 1000,
						  icon: "success",
						});
						this.collectorId =res.data;
					}
				})
			},
			// 取消收藏
			deleteCollector(){
				deleteCollectorById(this.collectorId).then(res => {
					if (res.code === 1) {
						uni.showToast({
						  title: '取消收成功',
						  duration: 1000,
						  icon: "none",
						});
						this.collectorId = null;
					}
				})
			},
			// 加入购物车
			goods_buy(){
				newAddShoppingCartAdd({
					setmealId: this.goods.id
				})
					.then((res) => {
						if (res.code === 1) {
							
							uni.showToast({
							  title: '加入购物车成功',
							  duration: 1000,
							  icon: "none",
							});
						}
					})
					.catch((err) => { })
			}
		
        
			
        }
    }
</script>

<style>
@import url(index.css);
</style>

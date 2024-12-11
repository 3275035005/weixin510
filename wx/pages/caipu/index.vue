<template>
    <view>
		
		<!-- tab -->
		<view class="page_tab flex five">
		    <view @click="tab_change(index)" class="item" :class="current_tab == index?'active':''" v-for="(item,index) in tab" :key="index">{{item.name}}</view>
		</view>
		<!-- tab -->
		
		 <view class="page_product flex">
		     <view class="item"  v-for="(item, index) in product" :key="index+'6'">
		         <navigator @click="()=>infoEvent(item)" hover-class="none">
		             <image class="item_pic" mode="aspectFill" :src="item.image"></image>
		             <view class="item_box">
		                  <view class="item_title">{{item.name}}</view>
		             </view>
		         </navigator>
		     </view>
		 </view>
    </view>
</template>

<script>
	import {getCategoryList, querySetmeaList} from "../api/api.js"
	import {mapMutations } from "vuex"
    export default {
        data() {
            return {
				current_tab: 0,
                tab:[],
                // product
                product:[]
                
            }
        },
		onLoad() {
			this.getInit();
		},
        methods: {
			//   vuex储存信息
			...mapMutations([
				"setToken"
			]),
			// tab
			tab_change(index){
			    this.current_tab = index
				this.getDishListDataes()
			},
			
			getInit(){
				getCategoryList('2').then((res) => {
					if (res && res.code === 1) {
						this.tab = [{id:'1', name:'推荐'}, ...res.data]
						this.getDishListDataes()
					}
				})
				
			},
			// 查询食谱
            getDishListDataes(){
			let param = {
				categoryId: this.tab[this.current_tab].id
			}
			 querySetmeaList(param)
					.then((success) => {
						this.product = success.data
					})
			},
			// 跳转详情
			infoEvent(item){		
				uni.navigateTo({
					url:'/pages/setmealdish_info/index?params='+encodeURIComponent(JSON.stringify(item))
				})
			}
        }
    }
</script>

<style>
@import url(index.css);
</style>

<template>
	<view class="navBar">
		
		<image class="index_bg" src="../../../static/bg.png"></image>
		
	</view>
</template>

<script>
export default {
	computed: {
		// ht: function () {
		// 	let res = uni.getMenuButtonBoundingClientRect()
		// 	let num = 24
		// 	if(/iPhone.*/.test(uni.getSystemInfoSync().model)){
		// 		 num = res.top * 1.6
		// 	} else {
		// 		num = res.top * 2
		// 	}
		// 	return num
		// }
		ht: function () {
			let res = uni.getMenuButtonBoundingClientRect() 
			return res.top +5
		}
	},
	methods: {
		myCenterFun () {
			uni.navigateTo({
				url: '/pages/my/my'
			})
		}
	}
}
</script>

<style src="./navbar.scss" lang="scss"></style>

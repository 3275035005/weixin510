<template>
  <view>
    <view class="my-center">
      <!-- 头像展示部分 -->
      <head-info
        :psersonUrl="psersonUrl"
        :nickName="nickName"
        :gender="gender"
        :phoneNumber="phoneNumber"
      ></head-info>

      <view class="container">
        <!-- 地址和历史订单 -->
        <order-info @goAddress="goAddress" @goOrder="goOrder" @goCollector="goCollector"
		@goInfo="goInfo"  @goShare="goShare"></order-info>
        <!-- 最近订单 -->
        <!-- 最近订单title -->
       <!-- <view
          class="recent"
          v-if="recentOrdersList && recentOrdersList.length > 0"
        >
          <text class="order_line">最近订单</text>
        </view> -->
       <!-- <order-list
          :scrollH="scrollH"
          @lower="lower"
          @goDetail="goDetail"
          @oneOrderFun="oneOrderFun"
          @getOvertime="getOvertime"
          @statusWord="statusWord"
          @numes="numes"
          :loading="loading"
          :loadingText="loadingText"
          :recentOrdersList="recentOrdersList"
        ></order-list> -->
      </view>
	  <view class="user_out page_box" @click="logOut">
	  	退出登录
	  </view>
    </view>
  </view>
</template>

<script>
import { getOrderPage, repetitionOrder, delShoppingCart, selectEmployeeById } from "../api/api.js";
import { mapMutations , mapState} from "vuex";
import { statusWord, getOvertime } from "@/utils/index.js";

import HeadInfo from "./components/headInfo.vue"; //头部
import OrderInfo from "./components/orderInfo.vue"; //地址
import OrderList from "./components/orderList.vue"; //最近订单
export default {
  data() {
    return {
      psersonUrl: "../../static/btn_waiter_sel.png",
      nickName: "未登录",
      gender: "0",
      phoneNumber: "111111111111",
      recentOrdersList: [],
      sumOrder: {
        amount: 0,
        number: 0,
      },
      status: "",
      scrollH: 0,
      pageInfo: {
        page: 1,
        pageSize: 10,
        total: 0,
      },
      loadingText: "",
      loading: false,
    };
  },
  components: {
    HeadInfo,
    OrderInfo,
    OrderList,
  },
  filters: {
  },
  onLoad() {
    this.getList();
	this.getUserInfo();
  },
  created() {},
  onReady() {
    uni.getSystemInfo({
      success: (res) => {
        this.scrollH = res.windowHeight - uni.upx2px(100);
      },
    });
  },
  onShow() {
  	if (!this.token()) {
  		uni.reLaunch({ 
  			url: "/pages/login/index"
  		})
  	}
  },
  methods: {
	  // 从vuex信息
	  ...mapState([
	  	"token"
	  ]),
    ...mapMutations(["setAddressBackUrl", "setToken"]),
    statusWord(obj) {
      return statusWord(obj.status, obj.time);
    },
	// 退出
	logOut(){
		wx.showModal({
		  title: '提示', //提示的标题
		  content: '确定退出登录吗', //提示的内容
		  success: function(res) {
		    if(res.confirm) {
				uni.reLaunch({
					url:'/pages/login/index'
				})
		    } else if (res.cancel) {
		      console.log('用户点击了取消')
		    }
		  }
		})
		
	},
	getUserInfo(){
		selectEmployeeById().then(res=>{
			this.nickName = res.data.name;
			this.phoneNumber = res.data.phone;
			this.gender = res.data.sex;
		})
	},
    getOvertime(time) {
      return getOvertime(time);
    },
    // 获取列表数据
    getList() {
      const params = {
        pageSize: 10,
        page: this.pageInfo.page,
      };
      getOrderPage(params).then((res) => {
        if (res.code === 1) {
          this.recentOrdersList = this.recentOrdersList.concat(
            res.data.records
          );
          this.pageInfo.total = res.data.total;
          this.loadingText = "";
          this.loading = false;
        }
      });
    },
    // 去地址页面
    goAddress() {
      this.setAddressBackUrl("/pages/my/my");
      // TODO
      uni.navigateTo({
        url: "/pages/address/address?form=" + "my",
      });
    },
    // 去历史订单页面
    goOrder() {
      // TODO
      uni.navigateTo({
        url: "/pages/historyOrder/historyOrder",
      });
    },
	// 个人收藏
	goCollector() {
	  uni.navigateTo({
	    url: "/pages/collector/index",
	  });
	},
	// 个人信息
	goInfo() {
	  uni.navigateTo({
	    url: "/pages/info/index",
	  });
	},
	//美食社区
	goShare(){
	  uni.navigateTo({
	    url: "/pages/share/index",
	  });
	},
    async oneOrderFun(id) {
      let pages = getCurrentPages();
      let routeIndex = pages.findIndex(
        (item) => item.route === "pages/index/index"
      );
      // 先清空购物车
      await delShoppingCart();
      repetitionOrder(id).then((res) => {
        if (res.code === 1) {
          uni.navigateBack({
            delta: routeIndex > -1 ? pages.length - routeIndex : 1,
          });
        }
      });
    },
    quitClick() {},
    // 去详情页面
    goDetail(id) {
      this.setAddressBackUrl("/pages/my/my");
      uni.redirectTo({
        url: "/pages/details/index?orderId=" + id,
      });
    },
    dataAdd() {
      const pages = Math.ceil(this.pageInfo.total / 10); //计算总页数
      if (this.pageInfo.page === pages) {
        this.loadingText = "没有更多了";
        this.loading = true;
      } else {
        this.pageInfo.page++;
        this.getList();
      }
    },

    lower() {
      this.loadingText = "数据加载中...";
      this.loading = true;
      this.dataAdd();
    },
    goBack() {
      uni.switchTab({
        url: "/pages/index/index",
      });
    },
  },
};
</script>
<style lang="scss" scoped>
	
	/* 退出登录 */
	.user_out{text-align: center; height: 100rpx; line-height: 100rpx; font-size: 28rpx;}
	.page_box{background: #fff; border-radius: 16rpx;  box-sizing: border-box; box-shadow: 0 2rpx 2rpx rgba(0,0,0,0.03);}
	
.my-center {
  background: #f6f6f6;
  height: 100%;

  .container {
    margin-top: 20rpx;
    height: calc(100% - 194rpx);
  }
}
::v-deep .uni-navbar--border {
  border-width: 0 !important;
}
</style>

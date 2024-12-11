<!--新增收获地址-->
<template>
  <view class="customer-box">
    <view
      class="add_edit"
      :style="{ height: `calc(100% - ${statusBarHeight} - 44px)` }"
    >
      <form class="form_address">
        <view class="uni-form-item uni-column form_item">
          <view class="title">姓名:</view>
          <uni-easyinput
            class="uni-input"
            v-model="form.name"
            placeholder-class="uni-place"
            placeholder="请填写姓名"
            minlength="2"
            maxlength="12"
          />
          <view class="radio">
            <view
              class="radio-item"
              v-for="(item, index) in items"
              :key="item.value"
              @click="sexChangeHandle(item.value)"
            >
              <image
                v-if="item.value != form.sex"
                class="radio-img"
                src="../../static/icon-radio.png"
              ></image>
              <image
                v-else
                class="radio-img"
                src="../../static/icon-radio-selected.png"
              ></image>
              <text class="radio-label">{{ item.name }}</text>
            </view>
          </view>
        </view>
        <view class="uni-form-item uni-column form_item">
          <view class="title">手机号:</view>
          <uni-easyinput
            class="uni-input"
            v-model="form.phone"
            type="number"
            placeholder-class="uni-place"
            placeholder="请填写手机号码"
            maxlength="11"
          />
        </view>
        <view class="uni-form-item uni-column form_item">
          <view class="title">地区:</view>
          <uni-easyinput
            class="uni-input"
            v-model="form.address"
            placeholder-class="uni-place"
            placeholder="请填写地区"
            minlength="2"
            maxlength="12"
          />
          </view>
        <view class="uni-form-item uni-column form_item tag-box">
          <view class="title">口味辣度:</view>
          <text
            :class="{ active: form.pungencyType === item.type }"
            class="tag_text"
            v-for="item in options"
            :key="item.type"
            @click="getTextOption(item)"
            >{{ item.name }}</text
          >
        </view>
		<view class="uni-form-item uni-column form_item tag-box">
		  <view class="title">口味甜度:</view>
		  <text
		    :class="{ active: form.sweetType === item.type }"
		    class="tag_text"
		    v-for="item in options1"
		    :key="item.type"
		    @click="getTextOption1(item)"
		    >{{ item.name }}</text
		  >
		</view>
      </form>

      <view class="add_address">
        <button
          class="add_btn"
          type="primary"
          plain="true"
          @click="addAddressFun()"
        >
          修改
        </button>
      </view>
    </view>
    
  </view>
</template>

<script>
import {
  selectEmployeeById,
  updateEmployee
} from "../api/api.js"

export default {
  data () {
    return {
      platform: "ios",
      showInput: true,
      showClass: false,
      items: [
        {
          value: "1",
          name: "男",
        },
        {
          value: "0",
          name: "女",
        },
      ],
      current: 0,
      options: [
        {
          name: "不辣",
          type: '0',
        },
        {
          name: "微辣",
          type: '1',
        },
        {
          name: "中辣",
          type: '2',
        },{
          name: "重辣",
          type: '3',
        }
      ],
	  options1: [
	    {
	      name: "无糖",
	      type: '0',
	    },
	    {
	      name: "少糖",
	      type: '1',
	    },
	    {
	      name: "半糖",
	      type:'2',
	    },
	    {
	      name: "多糖",
	      type: '3',
	    },
	    {
	      name: "全糖",
	      type: '4',
	    },
	  ],
      // type: 1,
      form: {
        name: "",
        phone: "",
        sweetType: 0,
		pungencyType:0,
        sex: "0",
        address: "11",

      },
    
      // 初始值
      address: "",
     
    }
  },
  onLoad (options) {
    this.init()
   
  },

  methods: {
    init () {
		selectEmployeeById().then(res=>{
			this.form = res.data;
		})
    },
    sexChangeHandle (val) {
      this.form.sex = val
    },
    // 保存地址
    addAddressFun () {
      if (this.form.name === "") {
        return uni.showToast({
          title: "姓名不能为空",
          duration: 1000,
          icon: "none",
        })
      } else if (this.form.phone === "") {
        return uni.showToast({
          title: "手机号不能为空",
          duration: 1000,
          icon: "none",
        })
      } else if (this.form.pungencyType === "") {
        return uni.showToast({
          title: "口味辣度不能为空",
          duration: 1000,
          icon: "none",
        })
      } else if (this.sweetType === "") {
        return uni.showToast({
          title: "口味甜度不能为空",
          duration: 1000,
          icon: "none",
        })
      }

      if (this.form.phone) {
        const reg =
          /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/
        if (!reg.test(this.form.phone)) {
          return uni.showToast({
            title: "手机号输入有误",
            duration: 1000,
            icon: "none",
          })
        }
      }
      if (this.form.name) {
        const reg = /^[\u0391-\uFFE5A-Za-z0-9]{2,12}$/
        if (!reg.test(this.form.name)) {
          return uni.showToast({
            title: "请输入合法的2-12个字符",
            duration: 1000,
            icon: "none",
          })
        }
      }
      const params = {
		id: this.form.id,
		name: this.form.name,
		phone: this.form.phone,
		pungencyType: this.form.pungencyType,
		sweetType: this.form.sweetType,
		sex: this.form.sex,
		address: this.form.address
      }
	  
	  // 修改资料
     updateEmployee(params).then((res) => {
       uni.showToast({
         title: '修改资料成功',
         duration: 1000,
         icon: "none",
       });
	   setTimeout(function() {
		uni.navigateBack({
			delta: 1
		})
	   }, 2000);
	   
     })
    },
    // 标签的事件
    getTextOption (item) {
      this.form.pungencyType = item.type
    },
	
	// 标签的事件
	getTextOption1 (item) {
	  this.form.sweetType = item.type
	}
  },
};
</script>

<style lang="scss" scoped>
.add_edit {
  width: 750rpx;
  height: 100%;
  background-color: #fff;
  .form_address {
    .form_item {
      margin: 0 22rpx;
      padding: 36rpx 0;
      border-bottom: 1px solid #efefef;
      display: flex;
      align-items: center;
      &.pad {
        padding-bottom: 10rpx;
        align-items: baseline;
      }
      .title {
        width: 140rpx;
        opacity: 1;
        font-size: 28rpx;
        font-family: PingFangSC, PingFangSC-Medium;
        font-weight: 600;
        text-align: left;
        color: #333333;
        letter-spacing: 0px;
        line-height: 44rpx;
      }
      /deep/ .is-input-border {
        border: 0 none;
        border-radius: none;
        min-height: auto;
        line-height: 16rpx;
        .uni-easyinput__content-input {
          padding-left: 0 !important;
          font-size: 26rpx;
        }
        .uni-easyinput__placeholder-class {
          font-size: 26rpx;
        }
        .uni-easyinput__content-textarea {
          padding: 18rpx 0 0;
          width: 100%;
          min-height: 60rpx;
          box-sizing: border-box;
          overflow: visible;
          height: auto;
          font-size: 26rpx;
        }
      }
      .uni-input {
        flex: 1;
      }
      /deep/ .uni-place {
        font-size: 26rpx;
        font-family: PingFangSC, PingFangSC-Regular;
        font-weight: 400;
        color: #999999 !important;
      }
      .radio {
        opacity: 1;
        font-size: 26rpx;
        font-family: PingFangSC, PingFangSC-Regular;
        font-weight: 400;
        text-align: left;
        color: #333333;
        letter-spacing: 0px;
        height: 40rpx;
        display: flex;
        padding-right: 20rpx;
        margin-left: 20rpx;
        .radio-item {
          display: flex;
          align-items: center;
          &:first-child {
            margin-right: 54rpx;
          }
        }
        .radio-img {
          width: 32rpx;
          height: 32rpx;
          margin-right: 10rpx;
        }
      }

      // 标签
      .tag_text {
        width: 68rpx;
        height: 44rpx;
        line-height: 40rpx;
        border: 1px solid #e5e4e4;
        display: inline-block;
        border-radius: 6rpx;
        text-align: center;
        box-sizing: border-box;
        color: #333333;
        font-size: 24rpx;
        &:nth-child(3) {
          margin: 0 20rpx;
        }
        &:nth-child(3) {
          &.active {
            background: #fef8e7;
            border: 1px solid #fef8e7;
          }
        }
        &:nth-child(4) {
          &.active {
            background: #e7fef8;
            border: 1px solid #e7fef8;
          }
        }
      }

      .active {
        background: #e1f1fe;
        border: 1px solid #e1f1fe;
      }
      .addressIcon {
        width: 24px;
        height: 24px;
        display: inline-block;
        position: absolute;
        right: 20rpx;
        top: 0;
        .icon {
          width: 16px;
          height: 16px;
          display: inline-block;
          background: url(../../static/toRight.png) no-repeat 6rpx 50%;
          background-size: contain;
          transform: rotate(90deg);
          margin-left: 10rpx;
        }
        .iconOn {
          transform: rotate(-90deg);
        }
      }

      .update-input {
        flex: 1;
      }
      .update-adress {
        position: relative;
        line-height: 40rpx;
        padding-bottom: 18rpx;
      }
    }
    // 详细地址
    .detail {
      width: 100%;
      height: 50rpx;
      margin: 0;
      /deep/ .uni-place {
        font-family: PingFangSC, PingFangSC-Regular;
        font-weight: 400;
        color: #999999;
        text-align: left;
        line-height: 50rpx;
      }
    }
    .detail-ios {
      padding: 20rpx 14rpx;
    }
  }
  .add_address {
    margin: 0 auto;
    button {
      margin-top: 40rpx;
      margin: 40rpx 18rpx 0 18rpx;
      height: 86rpx;
      line-height: 86rpx;
      border-radius: 8rpx;
      opacity: 1;
      font-size: 30rpx;
      font-family: PingFangSC, PingFangSC-Medium;
      font-weight: 600;
      text-align: center;
      color: #333333;
      letter-spacing: 0px;
      border: 0 none;
    }
    .add_btn {
      background: #ffc200;

      .img_btn {
        width: 44rpx;
        height: 44rpx;
        vertical-align: middle;
        margin-bottom: 8rpx;
      }
    }

    .del_btn {
      background: #f6f6f6;
    }
  }
}

.customer-box {
  height: 100vh;
}
/deep/ .uni-icons {
  font-size: 24px !important;
}
/deep/ .content-clear-icon {
  display: inline-block;
  width: 36rpx;
  height: 36rpx;
  line-height: 36rpx;
  padding-top: 4rpx;
  padding-bottom: 4rpx;
}
</style>
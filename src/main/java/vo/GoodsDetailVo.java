package vo;

import domain.MiaoshaUser;

public class GoodsDetailVo {
	
	private int miaoshaStatus;
	private int remianSeconds;
	private GoodsVo goods;
	private MiaoshaUser user;
	
	public MiaoshaUser getUser() {
		return user;
	}
	public void setUser(MiaoshaUser user) {
		this.user = user;
	}
	public int getMiaoshaStatus() {
		return miaoshaStatus;
	}
	public void setMiaoshaStatus(int miaoshaStatus) {
		this.miaoshaStatus = miaoshaStatus;
	}
	public int getRemianSeconds() {
		return remianSeconds;
	}
	public void setRemianSeconds(int remianSeconds) {
		this.remianSeconds = remianSeconds;
	}
	public GoodsVo getGoods() {
		return goods;
	}
	public void setGoods(GoodsVo goods) {
		this.goods = goods;
	}
	
}

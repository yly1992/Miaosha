package vo;

import java.util.Date;

import domain.Goods;

public class GoodsVo extends Goods {
	private Double miaosha_price;
	private Integer stock_count;
	private Date start_date;
	private Date end_date;

	public Double getMiaosha_price() {
		return miaosha_price;
	}

	public void setMiaosha_price(Double miaosha_price) {
		this.miaosha_price = miaosha_price;
	}

	public Integer getStock_count() {
		return stock_count;
	}

	public void setStock_count(Integer stock_count) {
		this.stock_count = stock_count;
	}

	public Date getStart_Date() {
		return start_date;
	}

	public void setStart_Date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_Date() {
		return end_date;
	}

	public void setEnd_Date(Date end_date) {
		this.end_date = end_date;
	}
}

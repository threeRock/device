package io.jianxun.web.dto;

import io.jianxun.domain.business.SparePart;

/**
 * 备件消耗情况
 * @author Administrator
 *
 */
public class SparePartUseDto {

	private SparePart sparePart;
	private int year;
	private int beforeStock;
	private int one;
	private int two;
	private int three;
	private int four;
	private int five;
	private int six;
	private int seven;
	private int eight;
	private int night;
	private int ten;
	private int eleven;
	private int twelve;
	private int afterStock;

	public SparePart getSparePart() {
		return sparePart;
	}

	public void setSparePart(SparePart sparePart) {
		this.sparePart = sparePart;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getBeforeStock() {
		return beforeStock;
	}

	public void setBeforeStock(int beforeStock) {
		this.beforeStock = beforeStock;
	}

	public int getOne() {
		return one;
	}

	public void setOne(int one) {
		this.one = one;
	}

	public int getTwo() {
		return two;
	}

	public void setTwo(int two) {
		this.two = two;
	}

	public int getThree() {
		return three;
	}

	public void setThree(int three) {
		this.three = three;
	}

	public int getFour() {
		return four;
	}

	public void setFour(int four) {
		this.four = four;
	}

	public int getFive() {
		return five;
	}

	public void setFive(int five) {
		this.five = five;
	}

	public int getSix() {
		return six;
	}

	public void setSix(int six) {
		this.six = six;
	}

	public int getSeven() {
		return seven;
	}

	public void setSeven(int seven) {
		this.seven = seven;
	}

	public int getEight() {
		return eight;
	}

	public void setEight(int eight) {
		this.eight = eight;
	}

	public int getNight() {
		return night;
	}

	public void setNight(int night) {
		this.night = night;
	}

	public int getTen() {
		return ten;
	}

	public void setTen(int ten) {
		this.ten = ten;
	}

	public int getEleven() {
		return eleven;
	}

	public void setEleven(int eleven) {
		this.eleven = eleven;
	}

	public int getTwelve() {
		return twelve;
	}

	public void setTwelve(int twelve) {
		this.twelve = twelve;
	}

	public int getAfterStock() {
		return afterStock;
	}

	public void setAfterStock(int afterStock) {
		this.afterStock = afterStock;
	}

}

package io.jianxun.domain;

/**
 * 
 * 逻辑删除标记接口
 * 
 * @author tongtn
 *
 *         createDate: 2017-03-15
 */
public interface Activeable {

	public Boolean isActive();

	public void setActive(Boolean active);

}

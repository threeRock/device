package io.jianxun.service.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import io.jianxun.domain.business.Depttest;
import io.jianxun.service.AbstractBaseService;
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.web.dto.DepttestTree;
import io.jianxun.web.utils.CurrentLoginInfo;

@Service
public class DepttestService extends AbstractBaseService<Depttest> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String ROOT_DEPART_NAME = "测试根部门";

	/**
	 * 创建机构根目录
	 * 
	 * @return
	 */
	@Transactional(readOnly = false)
	public Depttest initRoot() {
		Depttest depart;
		if (this.repository.count() == 0) {
			depart = new Depttest();
			depart.setName(ROOT_DEPART_NAME);
			return this.save(depart);
		}
		return this.repository.findActiveOne(1L);

	}

	public boolean validateNameUnique(String name, Long id) {
		return 0 == countActiveAll(DepttestPredicates.nameAndIdNotPredicate(name, id));
	}

//	/**
//	 * 返回用户机构树形结构
//	 * 
//	 * @return
//	 */
//	public List<DepartTree> getUserDepartTree() {
//		List<Depart> departs = getUserDepart();
//		return convertEntityToUserDepartTree(departs);
//	}
//
//	/**
//	 * 返回用户机构树形列表
//	 * 
//	 * @return
//	 */
//	public List<Depart> getUserDepart() {
//		Depart root = currentLoginInfo.currentLoginUser().getDepart();
//		if (root == null)
//			throw new BusinessException(localeMessageSourceService.getMessage("depart.currentuser.notfound"));
//		List<Depart> departs = Lists.newArrayList(root);
//		getSubDeparts(departs, root);
//		return departs;
//	}

	/**
	 * 返回机构树形结构
	 * 
	 * @return
	 */
	public List<DepttestTree> getDepttestTree() {
		Depttest root = this.initRoot();
		if (root == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depttest.currentuser.notfound"));
		List<Depttest> departs = Lists.newArrayList(root);
		getSubDeparts(departs, root);
		return convertEntityToDepartTree(departs);
	}

	/**
	 * 获取下级机构
	 * 
	 * @param root
	 * @return
	 */
	public void getSubDeparts(List<Depttest> container, Depttest root) {
		List<Depttest> ds = this.repository.findActiveAll(DepttestPredicates.parentPredicate(root),
				new Sort(Depttest.ID_NAME));
		if (!ds.isEmpty())
			container.addAll(ds);
		for (Depttest depart : ds) {
			getSubDeparts(container, depart);
		}
	}

//	private List<DepttestTree> convertEntityToUserDepartTree(List<Depttest> list) {
//		List<DepttestTree> tree = Lists.newArrayList();
//		for (Depttest depart : list) {
//			DepttestTree d = new DepttestTree();
//			d.setId(depart.getId());
//			if (depart.getParent() != null)
//				d.setpId(depart.getParent().getId());
//			d.setName(depart.getName());
//			d.setUrl("user/page");
//			d.setDivid("#user-page-layout");
//			tree.add(d);
//		}
//		return tree;
//	}

	private List<DepttestTree> convertEntityToDepartTree(List<Depttest> list) {
		List<DepttestTree> tree = Lists.newArrayList();
		for (Depttest depart : list) {
			DepttestTree d = new DepttestTree();
			d.setId(depart.getId());
			if (depart.getParent() != null)
				d.setpId(depart.getParent().getId());
			d.setName(depart.getName());
			tree.add(d);
		}
		return tree;
	}

//	@Autowired
//	private CurrentLoginInfo currentLoginInfo;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

}

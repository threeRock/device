package io.jianxun.service.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import io.jianxun.domain.business.Depart;
import io.jianxun.service.AbstractBaseService;
import io.jianxun.service.BusinessException;
import io.jianxun.service.LocaleMessageSourceService;
import io.jianxun.web.dto.DepartTree;
import io.jianxun.web.utils.CurrentLoginInfo;

@Service
public class DepartService extends AbstractBaseService<Depart> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String ROOT_DEPART_NAME = "气体公司";

	/**
	 * 创建机构根目录
	 * 
	 * @return
	 */
	@Transactional(readOnly = false)
	public Depart initRoot() {
		Depart depart;
		if (this.repository.count() == 0) {
			depart = new Depart();
			depart.setName(ROOT_DEPART_NAME);
			return this.save(depart);
		}
		return findActiveOne(DepartPredicates.namePredicate(ROOT_DEPART_NAME));

	}

	public boolean validateNameUnique(String name, Long id) {
		return 0 == countActiveAll(DepartPredicates.nameAndIdNotPredicate(name, id));
	}

	/**
	 * 返回用户机构树形结构
	 * 
	 * @return
	 */
	public List<DepartTree> getUserDepartTree() {
		Depart root = currentLoginInfo.currentLoginUser().getDepart();
		if (root == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.currentuser.notfound"));
		List<Depart> departs = Lists.newArrayList(root);
		getSubDeparts(Lists.newArrayList(), root);
		return convertEntityToUserDepartTree(departs);
	}

	/**
	 * 返回机构树形结构
	 * 
	 * @return
	 */
	public Object getDepartTree() {
		Depart root = currentLoginInfo.currentLoginUser().getDepart();
		if (root == null)
			throw new BusinessException(localeMessageSourceService.getMessage("depart.currentuser.notfound"));
		List<Depart> departs = Lists.newArrayList(root);
		getSubDeparts(departs,root);
		return convertEntityToDepartTree(departs);
	}

	/**
	 * 获取下级机构
	 * 
	 * @param root
	 * @return
	 */
	public void getSubDeparts(List<Depart> container, Depart root) {
		List<Depart> ds = this.repository.findActiveAll(DepartPredicates.parentPredicate(root),
				new Sort(Depart.ID_NAME));
		if (!ds.isEmpty())
			container.addAll(ds);
		for (Depart depart : ds) {
			getSubDeparts(container, depart);
		}
	}

	private List<DepartTree> convertEntityToUserDepartTree(List<Depart> list) {
		List<DepartTree> tree = Lists.newArrayList();
		for (Depart depart : list) {
			DepartTree d = new DepartTree();
			d.setId(depart.getId());
			if (depart.getParent() != null)
				d.setpId(depart.getParent().getId());
			d.setName(depart.getName());
			d.setUrl("user/page");
			d.setDivid("#user-page-layout");
			tree.add(d);
		}
		return tree;
	}

	private List<DepartTree> convertEntityToDepartTree(List<Depart> list) {
		List<DepartTree> tree = Lists.newArrayList();
		for (Depart depart : list) {
			DepartTree d = new DepartTree();
			d.setId(depart.getId());
			if (depart.getParent() != null)
				d.setpId(depart.getParent().getId());
			d.setName(depart.getName());
			tree.add(d);
		}
		return tree;
	}

	@Autowired
	private CurrentLoginInfo currentLoginInfo;
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

}

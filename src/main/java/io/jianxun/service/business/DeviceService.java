package io.jianxun.service.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;

import io.jianxun.domain.business.Depart;
import io.jianxun.domain.business.Device;
import io.jianxun.domain.business.DeviceDiscard;
import io.jianxun.domain.business.DeviceStatus;
import io.jianxun.repository.business.DeviceDiscardRepository;
import io.jianxun.service.AbstractBaseService;
import io.jianxun.web.dto.DepartTree;

@Service
public class DeviceService extends AbstractBaseService<Device> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Transactional(readOnly = false)
	public void discard(DeviceDiscard discard) {
		Device device = discard.getDevice();
		device.setStatus(DeviceStatus.DISCARD.getName());
		save(device);
		deviceDiscardRepository.save(discard);
	}

	public boolean validateNameUnique(String name, Depart depart, Long id) {
		return 0 == countActiveAll(ExpressionUtils.and(DevicePredicates.departPredicate(depart),
				DevicePredicates.nameAndIdNotPredicate(name, id)));
	}

	public boolean validateCodeUnique(String code, Depart depart, Long id) {
		return 0 == countActiveAll(ExpressionUtils.and(DevicePredicates.departPredicate(depart),
				DevicePredicates.codeAndIdNotPredicate(code, id)));
	}

	public boolean validateNameCodeUnique(String name, String code, Depart depart, Long id) {
		Predicate predicate = DevicePredicates.departPredicate(depart);
		predicate = ExpressionUtils.and(
				ExpressionUtils.and(predicate, DevicePredicates.nameAndIdNotPredicate(name, id)),
				DevicePredicates.codeAndIdNotPredicate(code, id));
		return 0 == countActiveAll(predicate);
	}

	public List<DepartTree> getDeviceTree() {
		return convertEntityToDepartTree(departService.getUserDepart());
	}

	// 验证设备信息是否可维护
	public boolean modifiable(Device device) {
		if (sparePartService.countActiveAll(SparePartPredicates.devicePredicate(device)) == 0
				&& deviceAdjustmentService.countActiveAll(DeviceAdjustmentPredicates.devicePredicate(device)) == 0
				&& deviceTechnicalParamService
						.countActiveAll(DeviceTechnicalParamPredicates.devicePredicate(device)) == 0
				&& deviceCheckInfoService.countActiveAll(DeviceCheckInfoPredicates.devicePredicate(device)) == 0
				&& deviceFaultService.countActiveAll(DeviceFaultPredicates.devicePredicate(device)) == 0)
			return true;
		return false;
	}

	private List<DepartTree> convertEntityToDepartTree(List<Depart> userDepart) {
		List<DepartTree> tree = Lists.newArrayList();
		for (Depart depart : userDepart) {
			DepartTree d = new DepartTree();
			d.setId(depart.getId());
			if (depart.getParent() != null)
				d.setpId(depart.getParent().getId());
			d.setName(depart.getName());
			d.setUrl("device/page");
			d.setDivid("#device-page-layout");
			tree.add(d);
		}
		return tree;
	}

	@Autowired
	private DepartService departService;
	@Autowired
	private DeviceDiscardRepository deviceDiscardRepository;
	@Autowired
	private DeviceFaultService deviceFaultService;
	@Autowired
	private DeviceCheckInfoService deviceCheckInfoService;
	@Autowired
	private DeviceTechnicalParamService deviceTechnicalParamService;
	@Autowired
	private DeviceAdjustmentService deviceAdjustmentService;
	@Autowired
	private SparePartService sparePartService;

}

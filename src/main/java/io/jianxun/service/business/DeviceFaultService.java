package io.jianxun.service.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jianxun.domain.business.DeviceFault;
import io.jianxun.service.AbstractBaseService;

@Service
@Transactional(readOnly = true)
public class DeviceFaultService extends AbstractBaseService<DeviceFault> {


}

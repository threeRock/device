package io.jianxun.domain.business;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.jianxun.web.dto.CodeNameDto;

//操作定义
public enum PermissionDef {
	// 用户管理相关权限
	USER_PAGE("USERLIST", "列表", ModuleDef.SYS, DomainDef.SYS_USER), USER_CREATE("USERCREATE", "新增", ModuleDef.SYS,
			DomainDef.SYS_USER), USER_MODIFY("USERMODIFY", "修改", ModuleDef.SYS, DomainDef.SYS_USER), USER_REMOVE(
					"USERREMOVE", "删除", ModuleDef.SYS, DomainDef.SYS_USER), USER_CHANGEPASSWROD("USERCHANGEPASSWROD",
							"修改密码", ModuleDef.SYS, DomainDef.SYS_USER), USER_RESETPASSWROD("USERRESETPASSWROD", "重置密码",
									ModuleDef.SYS, DomainDef.SYS_USER),
	// 角色
	ROLE_PAGE("ROLELIST", "列表", ModuleDef.SYS, DomainDef.SYS_ROLE), ROLE_CREATE("ROLECREATE", "新增", ModuleDef.SYS,
			DomainDef.SYS_ROLE), ROLE_MODIFY("ROLEMODIFY", "修改", ModuleDef.SYS,
					DomainDef.SYS_ROLE), ROLE_REMOVE("ROLEREMOVE", "删除", ModuleDef.SYS, DomainDef.SYS_ROLE),
	// 机构
	DEPART_PAGE("DEPARTLIST", "列表", ModuleDef.ORGANIZATION, DomainDef.ORG_DEPART), DEPART_CREATE("DEPARTCREATE", "新增",
			ModuleDef.ORGANIZATION, DomainDef.ORG_DEPART), DEPART_MODIFY("DEPARTMODIFY", "修改", ModuleDef.ORGANIZATION,
					DomainDef.ORG_DEPART), DEPART_REMOVE("DEPARTREMOVE", "删除", ModuleDef.ORGANIZATION,
							DomainDef.ORG_DEPART),
	// 设备类别
	MAINTYPE_PAGE("MAINTYPELIST", "列表", ModuleDef.DEVICE, DomainDef.DEVICE_MAINTYPE), MAINTYPE_CREATE("MAINTYPECREATE",
			"新增", ModuleDef.DEVICE, DomainDef.DEVICE_MAINTYPE), MAINTYPE_MODIFY("MAINTYPEMODIFY", "修改",
					ModuleDef.DEVICE, DomainDef.DEVICE_MAINTYPE), MAINTYPE_REMOVE("MAINTYPEREMOVE", "删除",
							ModuleDef.DEVICE, DomainDef.DEVICE_MAINTYPE),
	// 备件类别
	SUBTYPE_PAGE("SUBTYPELIST", "列表", ModuleDef.DEVICE, DomainDef.DEVICE_SUBTYPE), SUBTYPE_CREATE("SUBTYPECREATE", "新增",
			ModuleDef.DEVICE, DomainDef.DEVICE_SUBTYPE), SUBTYPE_MODIFY("SUBTYPEMODIFY", "修改", ModuleDef.DEVICE,
					DomainDef.DEVICE_SUBTYPE), SUBTYPE_REMOVE("SUBTYPEREMOVE", "删除", ModuleDef.DEVICE,
							DomainDef.DEVICE_SUBTYPE),
	// 仓库
	STOREHOUSE_PAGE("STOREHOUSELIST", "列表", ModuleDef.DEVICE, DomainDef.DEVICE_STOREHOUSE), STOREHOUSE_CREATE(
			"STOREHOUSECREATE", "新增", ModuleDef.DEVICE, DomainDef.DEVICE_STOREHOUSE), STOREHOUSE_MODIFY(
					"STOREHOUSEMODIFY", "修改", ModuleDef.DEVICE, DomainDef.DEVICE_STOREHOUSE), STOREHOUSE_REMOVE(
							"STOREHOUSEREMOVE", "删除", ModuleDef.DEVICE, DomainDef.DEVICE_STOREHOUSE),
	// 生产线
	PRODUCTIONLINE_PAGE("PRODUCTIONLINELIST", "列表", ModuleDef.DEVICE,
			DomainDef.DEVICE_PRODUCTIONLINE), PRODUCTIONLINE_CREATE("PRODUCTIONLINECREATE", "新增", ModuleDef.DEVICE,
					DomainDef.DEVICE_PRODUCTIONLINE), PRODUCTIONLINE_MODIFY("PRODUCTIONLINEMODIFY", "修改",
							ModuleDef.DEVICE, DomainDef.DEVICE_PRODUCTIONLINE), PRODUCTIONLINE_REMOVE(
									"PRODUCTIONLINEREMOVE", "删除", ModuleDef.DEVICE, DomainDef.DEVICE_PRODUCTIONLINE),
	// 设备
	DEVICE_PAGE("DEVICELIST", "列表", ModuleDef.DEVICE, DomainDef.DEVICE_DEVICE), DEVICE_CREATE("DEVICECREATE", "新增",
			ModuleDef.DEVICE, DomainDef.DEVICE_DEVICE), DEVICE_MODIFY("DEVICEMODIFY", "修改", ModuleDef.DEVICE,
					DomainDef.DEVICE_DEVICE), DEVICE_REMOVE("DEVICEREMOVE", "删除", ModuleDef.DEVICE,
							DomainDef.DEVICE_DEVICE),
	// 备件
	SPAREPART_PAGE("SPAREPARTLIST", "列表", ModuleDef.DEVICE, DomainDef.DEVICE_SPAREPART), SPAREPART_CREATE(
			"SPAREPARTCREATE", "新增", ModuleDef.DEVICE, DomainDef.DEVICE_SPAREPART), SPAREPART_MODIFY("SPAREPARTMODIFY",
					"修改", ModuleDef.DEVICE, DomainDef.DEVICE_SPAREPART), SPAREPART_REMOVE("SPAREPARTREMOVE", "删除",
							ModuleDef.DEVICE, DomainDef.DEVICE_SPAREPART);
	// 操作代码
	private String code;
	// 描述
	private String name;
	// 模块
	private ModuleDef module;

	// 模型代码
	private DomainDef domain;

	private PermissionDef(String code, String name, ModuleDef module, DomainDef domain) {
		this.code = code;
		this.name = name;
		this.module = module;
		this.domain = domain;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ModuleDef getModule() {
		return module;
	}

	public void setModule(ModuleDef module) {
		this.module = module;
	}

	public DomainDef getDomain() {
		return domain;
	}

	public void setDomain(DomainDef domain) {
		this.domain = domain;
	}

	private static Map<String, PermissionDef> valueMaps = Maps.newTreeMap();
	private static Map<String, List<CodeNameDto>> persMap = Maps.newTreeMap();
	private static Map<String, List<String>> initMap = Maps.newTreeMap();
	private static List<String> permissionCodeList = Lists.newArrayList();

	static {
		for (PermissionDef def : PermissionDef.values()) {
			// 系统设置权限组
			if (def.getDomain().equals(ModuleDef.SYS) || def.getDomain().equals(ModuleDef.ORGANIZATION)) {
				initValue("sys", def);
			}
			valueMaps.put(def.code, def);
			if (persMap.containsKey(def.getDomain().getCode())) {
				List<CodeNameDto> v = persMap.get(def.getDomain().getCode());
				v.add(converToCodeName(def));
			} else
				persMap.put(def.getDomain().getCode(), Lists.newArrayList(converToCodeName(def)));
			permissionCodeList.add(def.getCode());
		}
	}

	// 根据操作代码获取权限定义
	public static PermissionDef parse(String code) {
		return valueMaps.get(code);
	}

	private static void initValue(String key, PermissionDef def) {
		if (initMap.get(key) == null)
			initMap.put(key, Lists.newArrayList(def.getCode()));
		else
			initMap.get(key).add(def.getCode());

	}

	public static List<String> getPerGroup(String key) {
		return initMap.get(key);
	}

	private static CodeNameDto converToCodeName(PermissionDef def) {
		CodeNameDto dto = new CodeNameDto();
		dto.setCode(def.getCode());
		dto.setName(def.getName());
		return dto;
	}

	public static Map<String, List<CodeNameDto>> getPermission() {
		return persMap;
	}

	public static List<String> getPermissionCodeList() {
		return permissionCodeList;
	}

	public static void setPermissionCodeList(List<String> permissionCodeList) {
		PermissionDef.permissionCodeList = permissionCodeList;
	}

	// 模块定义
	public enum ModuleDef {
		SYS("sys", "系统设置", 99), ORGANIZATION("org", "机构管理", 7), DEVICE("device", "设备管理", 5);
		private String code;
		private String name;
		private Integer sortNum = 99;

		private ModuleDef(String code, String name, Integer sortNum) {
			this.code = code;
			this.name = name;
			this.sortNum = sortNum;
		}

		private static Map<String, ModuleDef> valueMaps = Maps.newHashMap();

		static {
			for (ModuleDef category : ModuleDef.values()) {
				valueMaps.put(category.code, category);
			}
		}

		public static ModuleDef parse(String code) {
			return valueMaps.get(code);
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getSortNum() {
			return sortNum;
		}

		public void setSortNum(Integer sortNum) {
			this.sortNum = sortNum;
		}

	}

	// 模型定义
	public enum DomainDef {
		// 系统配置
		SYS_USER("sys_user", "用户管理", 0), SYS_ROLE("sys_role", "角色管理", 10), ORG_DEPART("org_depart", "机构管理",
				20), DEVICE_MAINTYPE("device_maintype", "备件大类", 30), DEVICE_SUBTYPE("device_subtype", "备件子类",
						40), DEVICE_STOREHOUSE("device_storehouse", "仓库管理", 50), DEVICE_PRODUCTIONLINE(
								"device_productionline", "生产线管理", 60), DEVICE_DEVICE("device_device", "设备管理",
										70), DEVICE_SPAREPART("device_sparepart", "备件管理", 70);

		private String code;
		private String name;
		private Integer sortNum = 99;

		private DomainDef(String code, String name, Integer sortNum) {
			this.code = code;
			this.name = name;
			this.sortNum = sortNum;
		}

		private static Map<String, DomainDef> valueMaps = Maps.newHashMap();
		private static List<CodeNameDto> v = Lists.newArrayList();

		static {
			for (DomainDef def : DomainDef.values()) {
				if (def.getCode().startsWith("requestform_"))
					continue;
				valueMaps.put(def.code, def);
				v.add(converToCodeName(def));
			}
		}

		private static CodeNameDto converToCodeName(DomainDef def) {
			CodeNameDto dto = new CodeNameDto();
			dto.setCode(def.getCode());
			dto.setName(def.getName());
			return dto;
		}

		public static DomainDef parse(String code) {
			return valueMaps.get(code);
		}

		public static List<CodeNameDto> getDomainDefs() {
			return v;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getSortNum() {
			return sortNum;
		}

		public void setSortNum(Integer sortNum) {
			this.sortNum = sortNum;
		}

		@Override
		public String toString() {
			return this.name;
		}

	}

}

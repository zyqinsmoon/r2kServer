package com.apabi.r2k.admin.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.OrgPaperOrder;
import com.apabi.r2k.admin.model.PaperAuth;
import com.apabi.r2k.admin.service.OrgPaperOrderService;
import com.apabi.r2k.admin.service.PaperAuthService;
import com.apabi.r2k.common.base.PageRequestFactory;
import com.apabi.r2k.common.utils.DateUtil;
import com.apabi.r2k.common.utils.ExcelUtils;
import com.apabi.r2k.common.utils.FileUtils;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.RegexUtil;
import com.apabi.r2k.paper.service.SyncMessageService;
import com.apabi.r2k.security.model.AuthOrg;
import com.apabi.r2k.security.model.AuthUser;
import com.apabi.r2k.security.service.AuthOrgService;
import com.apabi.r2k.security.utils.SessionUtils;

@Controller("paperAuthAction")
@Scope("prototype")
public class PaperAuthAction {
	private Logger log = LoggerFactory.getLogger(PaperAuthAction.class);
	@Resource
	private PaperAuthService paperAuthService;
	@Resource
	private AuthOrgService authOrgService;
	@Resource
	private OrgPaperOrderService orgPaperOrderService;
	@Resource
	private SyncMessageService syncMessageService;

	private String orgId;
	private String type;
	private File resource;
	private String resourceFileName;
	private InputStream inputStream;
	private String msg;
	private List<PaperAuth> raList;
	private Page page;
	private int orderId;
	private String authStartDate;
	private String authEndDate;
	private String operator;
	private String crtDate;
	
	public static final String PAPERORDER_DATE_FORMAT = "yyyy/MM/dd";
	
	public String getCrtDate() {
		return crtDate;
	}

	public void setCrtDate(String crtDate) {
		this.crtDate = crtDate;
	}

	public OrgPaperOrder getOrgPaperOrder() {
		return orgPaperOrder;
	}

	public void setOrgPaperOrder(OrgPaperOrder orgPaperOrder) {
		this.orgPaperOrder = orgPaperOrder;
	}

	private OrgPaperOrder orgPaperOrder;

	private final long FILE_SIZE = 40 * 1024 * 1024;
	private Workbook wb = null;
	private String paperId = null;
	public final String PAPER_UNDUE = "1";// 未授权
	public final String PAPER_DUE = "2";// 已授权
	public final String PAPER_OUTDUE = "3";// 已过期
	public final String PAPER_DELETE = "4";// 已删除

	public String upload() throws Exception {
		System.out.println(authStartDate);
		return uploadExcel();
	}
	//订单删除
	public String deleteOrder() throws Exception {
//		orgPaperOrderService.deleteOrgPaperOrder(orderId, PAPER_DELETE);
//		paperAuthService.deletePaperAuthByorderId(orgId, orderId);
//		paperAuthService.deletePaperFilterInfo(
//				GlobalConstant.URL_FILTER_PAPER_DELETE,
//				paperAuthService.getPaperAuthByorderId(orgId, orderId));// 删除通知
		paperAuthService.deleteOrgPaperOrder(orgId, orderId, PAPER_DELETE);
		return showOrder();
	}
	//订单查询
	public String showOrder() throws Exception {
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if (orgId != null) {
				PageRequest pageRequest = new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest, req);
//				Map param = (Map) pageRequest.getFilters();
//				param.put("orgId", orgId);
				page = orgPaperOrderService.pageQuery(pageRequest);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "showOrder";
	}
	//明细查询
	public String showDetail() throws Exception {
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if (orgId != null) {
				PageRequest pageRequest = new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest, req);
				Map param = (Map) pageRequest.getFilters();
				param.put("orderId", orderId);
				page = paperAuthService.orderDetailQuery(pageRequest);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "showDetail";
	}
//+++++++++++++++++++++++++++++上传报纸授权重构+++++++++++++++++++++++++++++++++++++++++++++++++++
	//检查excel文件
	public int checkPaperExcel(File excel) throws Exception{
		if (excel == null) {
			return GlobalConstant.UPLOAD_FILE_TYPE_NOTFOUND;
		}
		if (!FileUtils.checkFileSize(excel, FILE_SIZE)) {

			return GlobalConstant.UPLOAD_FILE_TYPE_TOOLARGE;
		}
		if (!(resourceFileName.endsWith(".xls") || resourceFileName.endsWith(".xlsx"))) {
			return GlobalConstant.UPLOAD_FILE_TYPE_FORMATERROR;
		}
		return checkExcelData(excel);
	}
	
	//检查excel文件内容格式
	public int checkExcelData(File excel) throws Exception{
		raList = new ArrayList<PaperAuth>();
		InputStream in = new FileInputStream(excel);
		Workbook workBook = null;
		if(resourceFileName.endsWith(".xls")){
			workBook = (Workbook) new HSSFWorkbook(new POIFSFileSystem(in));
		}else if(resourceFileName.endsWith(".xlsx")){
			workBook = (Workbook) new XSSFWorkbook(in);
		}
		if(workBook == null){
			return GlobalConstant.UPLOAD_FILE_TYPE_FORMATERROR;
		}
		Sheet sht = workBook.getSheetAt(0);
		if(sht == null){
			return GlobalConstant.UPLOAD_FILE_TYPE_FORMATERROR;
		}
		int size = sht.getLastRowNum();
		for(int iRow = 1; iRow <= size; iRow++){
			Row row = sht.getRow(iRow);
			if(row == null){
				setMsg("第" + (iRow + 1) + "行:为空！");
				return GlobalConstant.UPLOAD_FILE_TYPE_DATAERROR;
			}
			//获取每行前四个单元格
			Cell paperCell = row.getCell(0);		
			Cell startCell = row.getCell(1);
			Cell endCell = row.getCell(2);
			Cell nameCell = row.getCell(3);
			String paperId = null;
			String strStartTime = null;
			Date startTime = null;
			String strEndTime = null;
			Date endTime = null;
			String paperName = null;
			//检查报纸id
			if(paperCell == null || StringUtils.isBlank(paperId = paperCell.getStringCellValue())){
				setMsg("第" + (iRow + 1) + "行:报纸ID为空！");
				return GlobalConstant.UPLOAD_FILE_TYPE_DATAERROR;
			}
			if(RegexUtil.match(paperId, "\\s")){
				setMsg("第" + (iRow + 1) + "行:报纸ID中含有字符！");
				return GlobalConstant.UPLOAD_FILE_TYPE_DATAERROR;
			}
			//检查资源可读开始时间
			if(startCell == null || StringUtils.isBlank(strStartTime = startCell.getStringCellValue())){
				setMsg("第" + (iRow + 1) + "行:资源内容开始时间为空！");
				return GlobalConstant.UPLOAD_FILE_TYPE_DATAERROR;
			}else{
				startTime = DateUtil.getDateFromString(strStartTime,PAPERORDER_DATE_FORMAT);
				if(startTime == null){
					setMsg("第" + (iRow + 1) + "行:资源内容开始时间格式错误！");
					return GlobalConstant.UPLOAD_FILE_TYPE_DATAERROR;
				}
			}
			//检查资源可读结束时间
			if(endCell == null || StringUtils.isBlank(strEndTime = endCell.getStringCellValue())){
				setMsg("第" + (iRow + 1) + "行:资源内容结束时间为空！");
				return GlobalConstant.UPLOAD_FILE_TYPE_DATAERROR;
			}else{
				endTime = DateUtil.getDateFromString(strEndTime,PAPERORDER_DATE_FORMAT);
				if(endTime == null){
					setMsg("第" + (iRow + 1) + "行:资源内容结束时间格式错误！");
					return GlobalConstant.UPLOAD_FILE_TYPE_DATAERROR;
				}
			}
			//检查资源可读结束时间是否比资源可读开始时间早
			if(startTime.after(endTime)){
				setMsg("第" + (iRow + 1) + "行:结束时间不能早于开始时间！");
				return GlobalConstant.UPLOAD_FILE_TYPE_DATAERROR;
			}
			//检查报纸名称是否为空
			if(nameCell == null || StringUtils.isBlank(paperName = nameCell.getStringCellValue())){
				setMsg("第" + (iRow + 1) + "行:报纸名称为空！");
				return GlobalConstant.UPLOAD_FILE_TYPE_DATAERROR;
			}
			PaperAuth pa = createPaperAuth(orgId, paperId, paperName, startTime, endTime);
			if(raList.contains(pa)){
				setMsg("第" + (iRow + 1) + "行:报纸重复！");
				return GlobalConstant.UPLOAD_FILE_TYPE_DATAERROR;
			}
			raList.add(pa);
		}
		return GlobalConstant.UPLOAD_FILE_TYPE_SUCCESS;
	}
	
	//创建PaperAuth
	public PaperAuth createPaperAuth(String orgId, String paperId, String paperName, Date start, Date end){
		PaperAuth pa = new PaperAuth();
		pa.setOrgId(orgId);
		pa.setPaperId(paperId);
		pa.setPaperName(paperName);
		pa.setReadStartDate(start);
		pa.setReadEndDate(end);
		pa.setType("0");
		return pa;
	}
	
	//根据订单信息填充报纸授权信息
	public void setPaperAuth(OrgPaperOrder order, PaperAuth paperAuth){
		paperAuth.setOrderId(order.getOrderId());
		paperAuth.setStatus(order.getStatus());
		Date date = new Date();
		paperAuth.setCrtDate(date);
		paperAuth.setLastDate(date);
	}
	
	//获取需要更新的报纸授权
	public List<PaperAuth> getNeedUpdatePaperAuth(List<PaperAuth> oldPaList, List<PaperAuth> newPaList){
		List<PaperAuth> updateList = new ArrayList<PaperAuth>();
		for(PaperAuth pa : newPaList){
			if(oldPaList.contains(pa)){
				pa.setStatus(PAPER_OUTDUE);
				updateList.add(pa);
			}
		}
		return updateList;
	}
	
	public String uploadExcel(){
		String rtnResult = "upload";
		HttpServletRequest req = ServletActionContext.getRequest();
		AuthUser currentUser = SessionUtils.getCurrentUser(req);
		String userName = currentUser.getUserName();
		if(!StringUtils.isBlank(orgId)){
			orgId = orgId.toLowerCase();
		}else{
			setMsg("机构id为空");
			return rtnResult;
		}
		try {
			Date authStartTime = DateUtil.getDateFromString(authStartDate,PAPERORDER_DATE_FORMAT);
			Date authEndTime = DateUtil.getDateFromString(authEndDate,PAPERORDER_DATE_FORMAT);
			if(authStartTime.after(authEndTime)){
				setMsg("授权开始时间大于授权结束时间，请修正！");
				return rtnResult;
			}
			if(resource == null){
				setMsg("文件不存在！");
				return rtnResult;
			}
			log.info("uploadExcel:开始检查报纸订单:"+resource.getName());
			int chkResult = checkPaperExcel(resource);
			if(chkResult != GlobalConstant.UPLOAD_FILE_TYPE_SUCCESS){
				if(chkResult != GlobalConstant.UPLOAD_FILE_TYPE_DATAERROR){
					setMsg(GlobalConstant.UPLOAD_FILE_MESSAGE[chkResult]);
				}
				return rtnResult;
			}
			boolean needPutFilter = false;
			OrgPaperOrder paperOrder = createPaperOrder(userName, authStartTime, authEndTime, orgId);
			// 授权立即生效情况
			if (DateUtil.isToday(authStartTime)) {
				needPutFilter = true;
				paperOrder.setStatus(PAPER_DUE);
				List<OrgPaperOrder> orgPaperOrderList = orgPaperOrderService.getOrgPaperOrderByStatus(orgId,PAPER_DUE);
				if(orgPaperOrderList != null && orgPaperOrderList.size() > 0){
					//查询报纸授权表
					List<PaperAuth> dbPaperAuthList = paperAuthService.getByStatus(orgId, PAPER_DUE);
					if(!(dbPaperAuthList == null || dbPaperAuthList.isEmpty())){
						List<PaperAuth> needUpdateAuths = getNeedUpdatePaperAuth(dbPaperAuthList, raList);		//获取设置为过期的报纸授权
						log.info("uploadExcel:[orgId#"+orgId+",userName#"+userName+"]:开始更新报纸授权为已过期");
						paperAuthService.batchUpdatePaperAuth(needUpdateAuths);		//更新报纸授权为已过期
					}
				}
			}else{
				paperOrder.setStatus(PAPER_UNDUE);
			}
			log.info("uploadExcel:[orgId#"+orgId+",userName#"+userName+"]:"+"开始保存报纸授权订单");
			orgPaperOrderService.saveOrgPaperOrder(paperOrder);
			for(PaperAuth pa : raList){
				setPaperAuth(paperOrder, pa);
			}
			log.info("uploadExcel:[orgId#"+orgId+",userName#"+userName+"]:"+"开始保存报纸授权订单明细");
			paperAuthService.batchSavePaperAuth(raList);
			if(needPutFilter){
				//开始创建filter
				log.info("uploadExcel:即时生效订单,开始创建filter");
				paperAuthService.createPaperFilterInfo(GlobalConstant.URL_FILTER_PAPER_CREATE,raList);
				//分发filter消息
				syncMessageService.distributeFilterMsg(orgId);
			}
			setMsg("保存订单成功");
		} catch (Exception e) {
			log.error("uploadExcel:上传授权异常:"+e.getMessage());
			setMsg("操作异常");
			log.error(e.getMessage(),e);
		}
		return rtnResult;
	}
	
	public OrgPaperOrder createPaperOrder(String operator, Date startDate, Date endDate,String orgId){
		OrgPaperOrder paperOrder = new OrgPaperOrder();
		paperOrder.setOrgId(orgId);
		paperOrder.setOperator(operator);
		paperOrder.setStartDate(startDate);
		paperOrder.setEndDate(endDate);
		paperOrder.setCrtDate(new Date());
		return paperOrder;
	}
	
//++++++++++++++++++++++++++++++上传报纸授权重构++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// 上传授权
	/*@SuppressWarnings("deprecation")
	public String uploadExcel() throws Exception {
		Date dateAuthDateStart = new Date(authStartDate);
		Date dateAuthDateEnd = new Date(authEndDate);
		Date now = new Date();
		operator = "";
		int counts = 0;
		// 上传时异常过滤
		String flags = uploadExceptionFilter();
		if (flags == "upload") {
			return "upload";
		}
		Sheet sheet = wb.getSheetAt(0);
		counts = sheet.getLastRowNum();
		if (dateAuthDateStart.after(dateAuthDateEnd)) {
			setMsg("授权开始时间大于授权结束时间，请修正！");
			return "upload";
		}
		// 授权立即生效情况
		if (dateAuthDateStart.getTime() - now.getTime() < 24 * 60 * 60 * 1000) {
			List<OrgPaperOrder> orgPaperOrderList = new ArrayList<OrgPaperOrder>();
			orgPaperOrderList = orgPaperOrderService.getOrgPaperOrderByStatus(orgId,PAPER_DUE);
			if (orgPaperOrderList.size() > 0) {
				// 查询报纸授权表
				List<PaperAuth> sorlPaperAuthList = new ArrayList<PaperAuth>();
				List<PaperAuth> dbPaperAuthList = new ArrayList<PaperAuth>();
				dbPaperAuthList = paperAuthService.getByStatus(orgId, PAPER_DUE);
					sheet = wb.getSheetAt(0);
					PaperAuth mypaperAuth = new PaperAuth();
					for (int i = 1; i <= sheet.getLastRowNum(); i++) {
						Row row = sheet.getRow(i);
						paperId = row.getCell(0).toString();
						if (paperId.trim().equals("") || row.getCell(1).toString().trim().equals("") || row.getCell(2).toString().trim().equals("") || row.getCell(3).toString().trim().equals("")) {
							setMsg("第" + (i + 1) + "行数据出错，请修正！");
							return "upload";
						}
						Date test = new Date(row.getCell(1).toString().trim());
						Date test1 = new Date(row.getCell(1).toString().trim());
						if (test.after(test1)) {
							setMsg("第" + (i + 1) + "行可阅开始时间大于可阅结束时间，请修正！");
							return "upload";
						}
						System.out.println(paperId);
						for (int j = 0, len = dbPaperAuthList.size(); j < len; j++) {
							mypaperAuth = dbPaperAuthList.get(j);
							if (mypaperAuth.getPaperId().toString()
									.trim().equals(paperId)) {
								// 数据库中设置以前的条目为已过期
								mypaperAuth.setStatus(PAPER_OUTDUE);
								paperAuthService.updatePaperAuth(mypaperAuth);
								// 准备通知sorl的列表
								sorlPaperAuthList.add(mypaperAuth);
							}
						}
					}
				// 添加订单表
				OrgPaperOrder orgPaperOrder1 = new OrgPaperOrder();
				setPaperOrderParameters(orgPaperOrder1, now, operator,
						dateAuthDateStart, dateAuthDateEnd, orgId, PAPER_DUE);
				orgPaperOrderService.saveOrgPaperOrder(orgPaperOrder1);

				// 添加授权表和通知sorl	
				int myOrderId1 = orgPaperOrder1.getOrderId();// 获取订单号
				List<PaperAuth> myPaperAuthList = addOrderTable(PAPER_DUE, myOrderId1, sheet);
				paperAuthService.addPaperAuth(myPaperAuthList);
				paperAuthService.createPaperFilterInfo(
						GlobalConstant.URL_FILTER_PAPER_CREATE,
						myPaperAuthList);// 创建通知
				setMsg("导入成功" + Integer.toString(counts) + "条!");
			} else {//机构不存在已生效的授权
				// 添加订单表
				OrgPaperOrder orgPaperOrder2 = new OrgPaperOrder();
				setPaperOrderParameters(orgPaperOrder2, now, operator,
						dateAuthDateStart, dateAuthDateEnd, orgId, PAPER_DUE);
				orgPaperOrderService.saveOrgPaperOrder(orgPaperOrder2);

				// 添加授权表和通知sorl
				List<PaperAuth> sorlPaperAuthList11 = new ArrayList<PaperAuth>();
				int myOrderId1 = orgPaperOrder2.getOrderId();// 获取订单号
				//sheet = wb.getSheetAt(0);
				sorlPaperAuthList11 = addOrderTable(PAPER_DUE, myOrderId1, sheet);
				paperAuthService.addPaperAuth(sorlPaperAuthList11);
				paperAuthService.createPaperFilterInfo(
						GlobalConstant.URL_FILTER_PAPER_CREATE,
						sorlPaperAuthList11);// 创建通知
				setMsg("导入成功" + Integer.toString(counts) + "条!");
			}
		} else {// 授权未生效情况
			// 添加订单表
			OrgPaperOrder orgPaperOrder3 = new OrgPaperOrder();
			setPaperOrderParameters(orgPaperOrder3, now, operator,
					dateAuthDateStart, dateAuthDateEnd, orgId, PAPER_UNDUE);
			orgPaperOrderService.saveOrgPaperOrder(orgPaperOrder3);
			// 添加授权表
			//sheet = wb.getSheetAt(0);
			int myOrderId1 = orgPaperOrder3.getOrderId();// 获取订单号
			List<PaperAuth> paperAuthList = addOrderTable(PAPER_UNDUE, myOrderId1, sheet);
			paperAuthService.addPaperAuth(paperAuthList);
			setMsg("导入成功" + Integer.toString(counts) + "条!");
		}	
		return "upload";
	}*/
	
	//添加授权表
	@SuppressWarnings("deprecation")
	public List<PaperAuth> addOrderTable(String status, int orderId  ,Sheet sheet) throws Exception {
		List<PaperAuth> paperAuthList = new ArrayList<PaperAuth>();
		Date now = new Date();
//		System.out.println(orderId);
		//Sheet sheet = wb.getSheetAt(0);
		int falggg = sheet.getLastRowNum();
		System.out.println(falggg);
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {

				Row row = sheet.getRow(i);
				paperId = row.getCell(0).toString();
				if (paperId.trim().equals("") || row.getCell(1).toString().trim().equals("") || row.getCell(2).toString().trim().equals("") || row.getCell(3).toString().trim().equals("")) {
					setMsg("第" + (i + 1) + "行数据出错，请修正！");
					return null;
				}				
				Date test = new Date(row.getCell(1).toString().trim());
				Date test1 = new Date(row.getCell(1).toString().trim());
				if (test.after(test1)) {
					setMsg("第" + (i + 1) + "行可阅开始时间大于可阅结束时间，请修正！");
					return null;
				}
				String resStartDate = row.getCell(1).toString();
				Date resStartDate1 = new Date(resStartDate);
				String resEndDate = row.getCell(2).toString();
				Date resEndDate1 = new Date(resEndDate);
				String paperName = row.getCell(3).toString();
				PaperAuth mypaperAuth = new PaperAuth();
				setPaperAuthParameters(mypaperAuth, orgId, orderId, now,
						now, paperId, resStartDate1, resEndDate1, status,
						paperName, type);
				paperAuthList.add(mypaperAuth);
			}
			return paperAuthList;
		}

	// 设置PaperAuth的全部参数
	public PaperAuth setPaperAuthParameters(PaperAuth paperAuth, String orgId,
			int orderId, Date crtDate, Date lastDate, String paperId,
			Date startDate, Date endDate, String status, String paperName,
			String type) {
		paperAuth.setOrgId(orgId);
		paperAuth.setOrderId(orderId);
		paperAuth.setCrtDate(crtDate);
		paperAuth.setLastDate(lastDate);
		paperAuth.setPaperId(paperId);
		paperAuth.setReadStartDate(startDate);
		paperAuth.setReadEndDate(endDate);
		paperAuth.setStatus(status);
		paperAuth.setPaperName(paperName);
		paperAuth.setType(type);
		return paperAuth;
	}

	// 设置Order订单的全部参数
	public void setPaperOrderParameters(OrgPaperOrder orgPaperOrder,
			Date crtDate, String operator, Date startDate, Date endDate,
			String orgId, String status) {
		orgPaperOrder.setCrtDate(crtDate);
		orgPaperOrder.setOperator(operator);
		orgPaperOrder.setStartDate(startDate);
		orgPaperOrder.setEndDate(endDate);
		orgPaperOrder.setOrgId(orgId);
		orgPaperOrder.setStatus(status);
	}

	// 过滤上传的异常
	public String uploadExceptionFilter() throws Exception {
		AuthOrg authOrg = null;
		try {
			authOrg = authOrgService.getOrgObject(orgId);
		} catch (Exception e1) {
			log.error(e1.getMessage(),e1);
			setMsg("服务器未响应");
			return "upload";
		}
		if (resource == null) {
			setMsg("未上传任何文件");
			return "upload";
		}

		if (authOrg == null) {
			setMsg("机构ID不存在");
			return "upload";
		}
		if (!FileUtils.checkFileSize(resource, FILE_SIZE)) {

			setMsg("文件太大，请重新选择上传文件。");
			return "upload";
		}
		try {
			InputStream in = new FileInputStream(resource);
			if (resourceFileName.endsWith(".xls")) {
				Workbook mywb = (Workbook) new HSSFWorkbook(
						new POIFSFileSystem(in));
				wb = mywb;
			} else if (resourceFileName.endsWith(".xlsx")) {
				Workbook mywb = (Workbook) new XSSFWorkbook(in);
				wb = mywb;
			} else {
				setMsg("文件格式不正确，请上传Excel文件");
				return "upload";
			}
		} catch (IOException e) {
			log.error(e.getMessage(),e);
			setMsg("文件导入失败");
			return "upload";
		}
		return "mysuccess";
	}

	public String show() {
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if (orgId != null) {
				log.info("分页查询开始");
				PageRequest pageRequest = new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest, req);
				Map param = (Map) pageRequest.getFilters();
				param.put("orgId", orgId);
				page = paperAuthService.pageQuery(pageRequest);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "show";
	}

	// 下载授权资源样例
	public String downExaple() {
		this.resourceFileName = "resourceExample.xlsx";
		try {
			inputStream = ServletActionContext.getServletContext()
					.getResourceAsStream(
							"/downExample/" + this.resourceFileName);
		} catch (Exception e1) {
			setMsg("下载授权资源样例错误");
			log.error("getInputStream:[]:[]:下载授权资源样例错误");
			log.error(e1.getMessage(),e1);
		}
		return "down";
	}

	//导出授权报纸列表
	public String export(){
		OutputStream os = null;
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if (orgId != null) {
				List<PaperAuth> paperlist = paperAuthService.getByStatus(orgId, PAPER_DUE);
				String filename = "paperAuthList.xls";
				String title = "已授权报纸列表";
				String[] headers = {"资源ID", "资源内容开始时间", "资源内容结束时间", "报纸名称"};
				// 设置输出的格式
				HttpServletResponse response = ServletActionContext.getResponse();
		        response.reset();
		        response.setContentType("bin");
		        response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
				os = response.getOutputStream();
				int row = paperlist.size();
				int col = 4;
				String[][] dataset = new String[row][col];
				int i = 0;
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				for (PaperAuth paper : paperlist) {
					int j = 0;
					dataset[i][j++] = paper.getPaperId();
					dataset[i][j++] = sf.format(paper.getReadStartDate());
					dataset[i][j++] = sf.format(paper.getReadEndDate());
					dataset[i][j++] = paper.getPaperName();
					i++;
				}
				new ExcelUtils().exportExcel(title, headers, dataset, os);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}finally{
			try {
				if(os != null){
					os.close();
					os = null;
				}
			} catch (IOException e) {
				log.error(e.getMessage(),e);
			}
		}
		return null;
	}
	
	
	// getter and setter
	public void setRaList(List<PaperAuth> raList) {
		this.raList = raList;
	}

	public List<PaperAuth> getRaList() {
		return raList;
	}

	public File getResource() {
		return resource;
	}

	public void setResource(File resource) {
		this.resource = resource;
	}

	public String getResourceFileName() {
		return resourceFileName;
	}

	public void setResourceFileName(String resourceFileName) {
		this.resourceFileName = resourceFileName;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getAuthStartDate() {
		return authStartDate;
	}

	public void setAuthStartDate(String authStartDate) {
		this.authStartDate = authStartDate;
	}

	public String getAuthEndDate() {
		return authEndDate;
	}

	public void setAuthEndDate(String authEndDate) {
		this.authEndDate = authEndDate;
	}
}

package com.fengchao.crm.workbench.service.impl;

import com.fengchao.crm.utils.DateTimeUtil;
import com.fengchao.crm.utils.SqlSessionUtil;
import com.fengchao.crm.utils.UUIDUtil;
import com.fengchao.crm.vo.PagintionVO;
import com.fengchao.crm.workbench.dao.*;
import com.fengchao.crm.workbench.domain.*;
import com.fengchao.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {


    //线索表
    private ClueDao  clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    //客户相关表
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    //联系人相关表
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    //交易相关表
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    //
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);


    public boolean sava(Clue c) {
        boolean flag = true;
        int count = clueDao.save(c);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    public PagintionVO<Clue> pageList(Map<String, Object> map) {
        //取得符合查询条件的总条数
        int total = clueDao.getTotalByCoundition(map);
        //取得线索列表
        List<Clue> dataList = clueDao.getClueListByCoundition(map);
        //把总条数和线索列表封装进VO里面
        PagintionVO<Clue> pagintionVO = new PagintionVO<Clue>();
        pagintionVO.setTotal(total);
        pagintionVO.setDataList(dataList);
        //返回VO
        return pagintionVO;
    }

    public Clue detail(String id) {
        Clue c = clueDao.detail(id);
        return c;
    }

    public List<ClueRemark> getRemarkListByAid(String clueId) {
        List<ClueRemark> cList = clueRemarkDao.getRemarkListByAid(clueId);
        return cList;
    }

    public Boolean unbund(String id) {
        Boolean flag = true;
        int count = clueActivityRelationDao.unbund(id);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    public Boolean bund(String cid, String[] aids) {
        Boolean flag =true;
        for (int i = 0; i <aids.length ; i++) {
            String aid = aids[i];
            String id = UUIDUtil.getUUID();
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(id);
            car.setClueId(cid);
            car.setActivityId(aid);
            int count = clueActivityRelationDao.bund(car);
            if (count!=1){
               flag=false;
            }
        }
        return flag;
    }

    public List<Activity> getActivityListByName(String aname) {
        List<Activity> aList = activityDao.getActivityListByName(aname);

        return aList;
    }

    public boolean convert(String clueId, Tran t, String createBy) {
        boolean flag = true;
        //String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        //通过线索的id查询线索对象（线索对象中封装了线索的信息）
        Clue c = clueDao.getById(clueId);
        //根据线索对象提取客户信息，当客户不存在时，新建客户（根据客户名称精确匹配，判断客户是否存在）
        String company = c.getCompany();
        Customer cus = customerDao.getCustomerByName(company);
        if (cus==null){
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setAddress(c.getAddress());
            cus.setWebsite(c.getWebsite());
            cus.setPhone(c.getPhone());
            cus.setOwner(c.getOwner());
            cus.setNextContactTime(c.getNextContactTime());
            cus.setName(company);
            cus.setDescription(c.getDescription());
            cus.setCreateTime(createTime);
            cus.setCreateBy(createBy);
            cus.setContactSummary(c.getContactSummary());
            //添加客户
            int count1 = customerDao.save(cus);
            if (count1!=1){
                flag = false;
            }
        }
            //通过线索对象提取联系人信息，保存联系人
        Contacts con = new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setSource(c.getSource());
        con.setOwner(c.getOwner());
        con.setNextContactTime(c.getNextContactTime());
        con.setMphone(c.getMphone());
        con.setJob(c.getJob());
        con.setFullname(c.getFullname());
        con.setEmail(c.getEmail());
        con.setDescription(c.getDescription());
        con.setCustomerId(cus.getId());
        con.setCreateTime(createTime);
        con.setCreateBy(createBy);
        con.setContactSummary(c.getContactSummary());
        con.setAppellation(c.getAppellation());
        int count2 = contactsDao.save(con);
        if (count2!=1){
            flag=false;
        }
        //经过第三步的处理，联系人的信息已经有了，如果需要联系人Id 则直接  con.getId
        //线索的备注转移到客户备注和联系人备注
        List<ClueRemark> crList = clueRemarkDao.getRemarkListByAid(clueId);
        //取出每一条线索的备注
        for(ClueRemark clueRemark:crList) {
            //取出备注信息
            String noteContent = clueRemark.getNoteContent();
            //添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(cus.getId());
            customerRemark.setEditFlag("0");
            int count3 = customerRemarkDao.save(customerRemark);
            if (count3!=1){
                flag = false;
            }
            //添加联系人备注
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(con.getId());
            contactsRemark.setEditFlag("0");
            int count4 = contactsRemarkDao.save(contactsRemark);
            if (count4!=1){
                flag = false;
            }
        }
        //线索和市场活动的关系，转换为联系人和市场活动的关系
        //查询出来该条线索关联的市场活动，
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        for (ClueActivityRelation clueActivityRelation:clueActivityRelationList){
            String activityId = clueActivityRelation.getActivityId();
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(con.getId());
            contactsActivityRelation.setActivityId(activityId);
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count5!=1){
                flag = false;
            }
        }
        //如果有创建交易的需求，则需要创建一条交易，可以通过从控制器传过来的参数t 是否为NULL来判断是否要创建交易
        if (t!=null){
            //创建交易
            /**
             * t对象在controller中已经封装了一些信息进去了
             *  id,money,name,expectedDate,stage,activityId,createBy,createTime
             */
            t.setSource(c.getSource());
            t.setOwner(c.getOwner());
            t.setNextContactTime(c.getNextContactTime());
            t.setDescription(c.getDescription());
            t.setCustomerId(cus.getId());
            t.setContactSummary(c.getContactSummary());
            t.setContactsId(con.getId());
            int count6 = tranDao.save(t);
            if (count6!=1){
                flag = false;
            }
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setTranId(t.getId());
            tranHistory.setStage(t.getStage());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setCreateTime(createTime);
            tranHistory.setCreateBy(createBy);
            int count7 = tranHistoryDao.save(tranHistory);
            if (count7!=1){
                flag = false;
            }
        }

        //删除线索备注
//        for(ClueRemark clueRemark:crList) {
//            int count8 = clueRemarkDao.delete(clueRemark);
//            if (count8!=1){
//                flag = false;
//            }
//        }
        //删除线索和市场活动的关系
//        for (ClueActivityRelation clueActivityRelation:clueActivityRelationList){
//            int count9 = clueActivityRelationDao.delete(clueActivityRelation);
//            if (count9!=1){
//                flag = false;
//            }
//        }
        //删除线索
//        int count10 = clueDao.delete(clueId);
//        if (count10!=1){
//            flag = false;
//        }
        return flag;
    }


}

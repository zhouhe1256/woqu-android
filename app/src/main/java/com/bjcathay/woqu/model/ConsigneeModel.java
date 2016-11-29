package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.io.Serializable;

/**
 * Created by jiangm on 15-10-12.
 */
public class ConsigneeModel implements Serializable{

    private String name;// 收货人
    private String contactWay;// 联系方式
    private String consigneeTime;// 收货时间
    private long luProvinceId;// 省份ID
    private long luCityId;// 城市ID
    private long luCountyId;// 区县ID
    private String detailAddress;// 详细地址
    private boolean def;//默认地址
    private long id;
    private String zipCode;

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public boolean isDef() {
        return def;
    }

    public void setDef(boolean def) {
        this.def = def;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getConsigneeTime() {
        return consigneeTime;
    }

    public void setConsigneeTime(String consigneeTime) {
        this.consigneeTime = consigneeTime;
    }

    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public long getLuCityId() {
        return luCityId;
    }

    public void setLuCityId(long luCityId) {
        this.luCityId = luCityId;
    }

    public long getLuCountyId() {
        return luCountyId;
    }

    public void setLuCountyId(long luCountyId) {
        this.luCountyId = luCountyId;
    }

    public long getLuProvinceId() {
        return luProvinceId;
    }

    public void setLuProvinceId(long luProvinceId) {
        this.luProvinceId = luProvinceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static IContentDecoder<ConsigneeModel> decoder = new IContentDecoder.BeanDecoder<ConsigneeModel>(
            ConsigneeModel.class);

    // 添加收货人
    public static IPromise newCons(String name,String contactWay,long luProvinceId,
                                   long luCityId, long luCountyId ,String detailAddress,String zipCode ) {
        return Http.instance().post(ApiUrl.CONSIGINESS_NEW).param("name", name)
                .param("contactWay",contactWay)
                .param("zipCode",zipCode)
               // .param("consigneeTime", consigneeTime)
                .param("luProvinceId",luProvinceId)
                .param("luCityId", luCityId)
                .param("luCountyId",luCountyId)
                .param("detailAddress",detailAddress).isCache(false).run();
    }
    // 修改收货人
    public static IPromise modifyCons(Long id,String name,String contactWay,long luProvinceId,
                                      long luCityId, long luCountyId ,String detailAddress ,String zipCode) {
        return Http.instance().put(ApiUrl.modifyConsignee(id))
                .param("name", name)
                .param("contactWay",contactWay)
               // .param("consigneeTime",consigneeTime)
                .param("zipCode", zipCode)
                .param("luProvinceId", luProvinceId)
                .param("luCityId", luCityId)
                .param("luCountyId", luCountyId)
                .param("detailAddress", detailAddress).isCache(false).run();
    }
    // 设置为默认收货人
    public static IPromise defaultCons(Long id) {
        return Http.instance().put(ApiUrl.defaultConsignee(id)).isCache(false).run();
    }
    // 删除收货人
    public static IPromise deleteCons(Long id) {
        return Http.instance().post(ApiUrl.deleteConsignee(id)).param("_method","DELETE").isCache(false).run();
    }
}

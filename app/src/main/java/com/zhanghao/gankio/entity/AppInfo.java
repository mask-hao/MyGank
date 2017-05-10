package com.zhanghao.gankio.entity;

import com.google.gson.Gson;

/**
 * Created by zhanghao on 2017/5/9.
 */

public class AppInfo {

    /**
     * name : GankIo
     * version : 1
     * changelog :
     * updated_at : 1494168053
     * versionShort : 1.0
     * build : 1
     * installUrl : http://download.fir.im/v2/app/install/590f316f548b7a04960000c3?download_token=f517c396ac0e84ff3a4cab8f8c48224b&source=update
     * install_url : http://download.fir.im/v2/app/install/590f316f548b7a04960000c3?download_token=f517c396ac0e84ff3a4cab8f8c48224b&source=update
     * direct_install_url : http://download.fir.im/v2/app/install/590f316f548b7a04960000c3?download_token=f517c396ac0e84ff3a4cab8f8c48224b&source=update
     * update_url : http://fir.im/skq1
     * binary : {"fsize":4764880}
     */

    private String name;
    private String version;
    private String changelog;
    private int updated_at;
    private String versionShort;
    private int build;
    private String installUrl;
    private String install_url;
    private String direct_install_url;
    private String update_url;
    private BinaryBean binary;

    public static AppInfo objectFromData(String str) {

        return new Gson().fromJson(str, AppInfo.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public int getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(int updated_at) {
        this.updated_at = updated_at;
    }

    public String getVersionShort() {
        return versionShort;
    }

    public void setVersionShort(String versionShort) {
        this.versionShort = versionShort;
    }

    public int getBuild() {
        return build;
    }

    public void setBuild(int build) {
        this.build = build;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }

    public String getInstall_url() {
        return install_url;
    }

    public void setInstall_url(String install_url) {
        this.install_url = install_url;
    }

    public String getDirect_install_url() {
        return direct_install_url;
    }

    public void setDirect_install_url(String direct_install_url) {
        this.direct_install_url = direct_install_url;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public BinaryBean getBinary() {
        return binary;
    }

    public void setBinary(BinaryBean binary) {
        this.binary = binary;
    }

    public static class BinaryBean {
        /**
         * fsize : 4764880
         */

        private long fsize;

        public static BinaryBean objectFromData(String str) {

            return new Gson().fromJson(str, BinaryBean.class);
        }

        public long getFsize() {
            return fsize;
        }

        public void setFsize(long fsize) {
            this.fsize = fsize;
        }
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", changelog='" + changelog + '\'' +
                ", updated_at=" + updated_at +
                ", versionShort='" + versionShort + '\'' +
                ", build=" + build +
                ", installUrl='" + installUrl + '\'' +
                ", install_url='" + install_url + '\'' +
                ", direct_install_url='" + direct_install_url + '\'' +
                ", update_url='" + update_url + '\'' +
                ", binary=" + binary +
                '}';
    }
}

package com.duowei.kitchen_barbecue.httputils;

/**
 * Created by Administrator on 2017-09-04.
 */

public class Net {
    public static String url="http://192.168.1.78:2233/server/ServerSvlt?";
//    public static String sqlCfpb="select A.XH,A.xmbh,LTrim(A.xmmc)as xmmc,A.dw,(isnull(A.sl,0)-isnull(A.tdsl,0)-isnull(A.YWCSL,0))sl," +
//            "A.pz,CONVERT(varchar(100), a.xdsj, 120)as xdsj,A.BY1 as czmc,datediff(minute,A.xdsj,getdate())fzs,A.yhmc,isnull(A.xszt,'')xszt," +
//            "A.ywcsl,j.py,isnull(j.by13,9999999)cssj,A.by9,A.by10 from cfpb A LEFT JOIN JYXMSZ J ON A.XMBH=J.XMBH where A.XDSJ BETWEEN DATEADD(mi,-180,GETDATE()) " +
//            "AND GETDATE() and (isnull(A.sl,0)-isnull(A.tdsl,0))>0 and a.pos='cyy'order by A.xdsj,A.xmmc|";
    public static String sqlJyxmsz="select xmbh,xmmc,py,isnull(gq,'0')gq from jyxmsz where isnull(sfqx,'0')<>'1' and isnull(sftc,'0')<>'1'|";

    public static String sqlCfpb="select A.XH,A.xmbh,LTrim(A.xmmc)as xmmc,A.dw,(isnull(A.sl,0)-isnull(A.tdsl,0)-isnull(A.YWCSL,0))sl," +
            "A.pz,CONVERT(varchar(100), a.xdsj, 120)as xdsj,A.BY1 as czmc,datediff(minute,A.xdsj,getdate())fzs,A.yhmc,isnull(A.xszt,'')xszt," +
            "A.ywcsl,j.py,isnull(j.by13,9999999)cssj,A.by9,A.by10 from cfpb A LEFT JOIN JYXMSZ J ON A.XMBH=J.XMBH where A.XDSJ BETWEEN DATEADD(mi,-180,GETDATE()) " +
            "AND GETDATE() and (isnull(A.sl,0)-isnull(A.tdsl,0))>0 and a.pos='cyy'order by (case when (datediff(minute,A.xdsj,getdate())-case when isnull(j.by13,9999999)='' then 999999 else isnull(j.by13,9999999) end)<0 then 0 else (datediff(minute,A.xdsj,getdate())-case when isnull(j.by13,9999999)='' then 999999 else isnull(j.by13,9999999) end) end)desc,A.xdsj,A.xmmc|";
}

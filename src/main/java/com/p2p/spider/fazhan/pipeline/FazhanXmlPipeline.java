package com.p2p.spider.fazhan.pipeline;

import com.p2p.spider.common.FileMode;
import com.p2p.spider.fazhan.module.Invest;
import com.p2p.spider.fazhan.module.InvestSet;
import org.apache.commons.collections.CollectionUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;
import java.util.List;

/**
 * flush the data to xml
 *
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-3-23
 */
public class FazhanXmlPipeline implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private final String filePath;
    private FileMode fileMode;
    private InvestSet investSet;
    public FazhanXmlPipeline(String filePath) {
        this(filePath, FileMode.OVERWRITE);
    }
    public FazhanXmlPipeline(String filePath,FileMode fileMode) {
        this.filePath = filePath;
        this.fileMode = fileMode;
        if (fileMode == FileMode.UPDATE) {
            this.investSet = parse();
        }
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        if (fileMode == FileMode.OVERWRITE) {
            investSet = resultItems.get("investSet");
        } else {
            Invest invest = resultItems.get("invest");
            merge(investSet, invest);
        }
        flush(investSet);
    }

    private void merge(InvestSet investSet, Invest invest) {
        List<Invest> investList = investSet.getInvests();
        if (investSet != null&&CollectionUtils.isNotEmpty(investList)) {
            for (Invest oriInvest : investList) {
                if (oriInvest.getInvestId().equals(invest.getInvestId())) {
                    oriInvest.setDesc(invest.getDesc());
                    oriInvest.setMinInvest(invest.getMinInvest());
                    oriInvest.setMaxInvest(invest.getMaxInvest());
                }
            }
        }
    }

    /**
     * write {@code InvestSet} to file
     * @param investSet
     */
    public void flush(InvestSet investSet) {
        Serializer serializer = new Persister();
        File xmlFile = new File(filePath);
        try {
            serializer.write(investSet, xmlFile);
        } catch (Exception e) {
            logger.error("flush to file error:"+filePath,e);
        }
    }

    /**
     * read {@code InvestSet} from file
     * @return
     */
    public InvestSet parse() {
        Serializer serializer = new Persister();
        File xmlFile = new File(filePath);
        try {
            InvestSet investSet = serializer.read(InvestSet.class, xmlFile);
            return investSet;
        } catch (Exception e) {
            logger.error("flush to file error:"+filePath,e);
        }
        return null;
    }
}

package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.rest.api.film.FilmAsyncServiceAPI;
import com.stylefeng.guns.rest.api.film.vo.*;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/4 11:35 下午
 * @Modified By:
 */
@Component
@Service(interfaceClass = FilmAsyncServiceAPI.class)
public class DefaultFilmAsyncServiceImpl implements FilmAsyncServiceAPI {

    private final ActorTMapper actorTMapper;
    private final FilmInfoTMapper filmInfoTMapper;

    @Autowired
    public DefaultFilmAsyncServiceImpl(ActorTMapper actorTMapper, FilmInfoTMapper filmInfoTMapper) {
        this.actorTMapper = actorTMapper;
        this.filmInfoTMapper = filmInfoTMapper;
    }

    /**
     * 从数据库根据filmId查询 FilmInfoT 对象
     * @param filmId
     * @return
     */
    private FilmInfoT getFilmInfo(String filmId) {
        FilmInfoT filmInfoT = new FilmInfoT();
        filmInfoT.setFilmId(filmId);
        return filmInfoTMapper.selectOne(filmInfoT);
    }

    /**
     * 获取影片描述信息
     * @param filmId
     * @return
     */
    @Override
    public FilmDescVO getFilmDesc(String filmId) {
        FilmDescVO filmDescVO = new FilmDescVO();
        FilmInfoT filmInfoT = getFilmInfo(filmId);
        filmDescVO.setBiography(filmInfoT.getBiography());
        filmDescVO.setFilmId(filmId);
        return filmDescVO;
    }

    /**
     * 获取影片图片地址
     * @param filmId
     * @return
     */
    @Override
    public ImgVO getImgs(String filmId) {
        ImgVO imgVO = new ImgVO();
        FilmInfoT filmInfoT = getFilmInfo(filmId);
        String filmImgs = filmInfoT.getFilmImgs();
        //图片地址是5个以","分割的url
        String[] imgs = filmImgs.split(",");
        int i = 0;
        imgVO.setMainImg(imgs[i++]);
        imgVO.setImg01(imgs[i++]);
        imgVO.setImg02(imgs[i++]);
        imgVO.setImg03(imgs[i++]);
        imgVO.setImg04(imgs[i]);
        return imgVO;
    }

    /**
     * 获取导演信息
     * @param filmId
     * @return
     */
    @Override
    public ActorVO getDirector(String filmId) {
        ActorVO actorVO = new ActorVO();
        FilmInfoT filmInfoT = getFilmInfo(filmId);
        //获取导演编号
        Integer directorId = filmInfoT.getDirectorId();
        ActorT actorT = actorTMapper.selectById(directorId);
        actorVO.setDirectorName(actorT.getActorName());
        actorVO.setImgAddress(actorT.getActorImg());
        return actorVO;
    }

    /**
     * 获取演员信息
     * @param filmId
     * @return
     */
    @Override
    public List<ActorVO> getActors(String filmId) {
        return actorTMapper.getActors(filmId);
    }
}

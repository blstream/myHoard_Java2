package com.blstream.myhoard.controller;

import com.blstream.myhoard.biz.model.CollectionDTO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/favourites")
public class FavouriteController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<CollectionDTO> getFavourites(HttpServletRequest request) {
        return null;
    }
}

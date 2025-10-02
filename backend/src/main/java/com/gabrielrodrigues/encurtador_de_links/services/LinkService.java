package com.gabrielrodrigues.encurtador_de_links.services;

import com.gabrielrodrigues.encurtador_de_links.dtos.ResponseShortenLinkDto;
import com.gabrielrodrigues.encurtador_de_links.models.User;
import com.gabrielrodrigues.encurtador_de_links.repositories.LinkRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class LinkService {
    private LinkRepository linkRepository;
    private UserService userService;

    public LinkService(LinkRepository linkRepository, UserService userService) {
        this.linkRepository = linkRepository;
        this.userService = userService;
    }

    public ResponseShortenLinkDto createShortLink(String url, HttpServletRequest request, Long userId) {
//        User user = this.userService.getById(userId);
        return null;
    }

//    public String generateNewShortLink()
}

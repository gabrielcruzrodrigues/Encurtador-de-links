package com.gabrielrodrigues.encurtador_de_links.services;

import com.gabrielrodrigues.encurtador_de_links.dtos.ResponseShortenLinkDto;
import com.gabrielrodrigues.encurtador_de_links.exceptions.customExceptions.EntityNotFoundException;
import com.gabrielrodrigues.encurtador_de_links.models.Link;
import com.gabrielrodrigues.encurtador_de_links.models.User;
import com.gabrielrodrigues.encurtador_de_links.repositories.LinkRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class LinkService {
    private final LinkRepository linkRepository;
    private final UserService userService;

    public LinkService(LinkRepository linkRepository, UserService userService) {
        this.linkRepository = linkRepository;
        this.userService = userService;
    }

    @Transactional
    public ResponseShortenLinkDto createShortUrl(String originalUrl, HttpServletRequest request, Long userId) {
        User user = this.userService.getById(userId);
        String shortUrlPath = this.generateNewShortUrl();
        String appUrl = request.getScheme() + "://short.local";
        String fullShortUrl = appUrl + "/" + shortUrlPath;

        Link link = new Link(
                fullShortUrl,
                originalUrl,
                user
        );

        try {
            this.linkRepository.save(link);

            return new ResponseShortenLinkDto(
                    link.getShortUrl(),
                    link.getOriginalUrl()
            );
        } catch(DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Não foi possível salvar o link: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao salvar o link!");
        }
    }

    private String generateNewShortUrl() {
        String randomShortUrl = UUID.randomUUID().toString().substring(0,8);

        boolean linkVerify = true;
        do {
            linkVerify = this.linkRepository.findByShortUrl(randomShortUrl).isPresent();
        } while (linkVerify);

        return randomShortUrl;
    }

    public String findOriginalUrlByShortUrl(String shortUrl) {
        String fullShortUrl = "http://short.local/" + shortUrl;
        Link link = this.linkRepository.findByShortUrl(fullShortUrl).orElseThrow(
                () -> new EntityNotFoundException("A shortUrl: " + shortUrl + " não foi encontrada!"));

        return link.getOriginalUrl();
    }
}

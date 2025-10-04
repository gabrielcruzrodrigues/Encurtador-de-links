package com.gabrielrodrigues.encurtador_de_links.services;

import com.gabrielrodrigues.encurtador_de_links.dtos.ResponseShortShortenLinkDto;
import com.gabrielrodrigues.encurtador_de_links.exceptions.customExceptions.DeleteFailedException;
import com.gabrielrodrigues.encurtador_de_links.exceptions.customExceptions.EntityNotFoundException;
import com.gabrielrodrigues.encurtador_de_links.models.Link;
import com.gabrielrodrigues.encurtador_de_links.models.User;
import com.gabrielrodrigues.encurtador_de_links.repositories.LinkRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class LinkService {
    private final LinkRepository linkRepository;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(LinkService.class);

    @Value("${spring.app.redirectBasePath}")
    private String redirectBasePath;

    public LinkService(LinkRepository linkRepository, UserService userService) {
        this.linkRepository = linkRepository;
        this.userService = userService;
    }

    @Transactional
    public ResponseShortShortenLinkDto createShortUrl(String originalUrl, HttpServletRequest request, Long userId) {
        User user = this.userService.getById(userId);
        String shortUrlPath = this.generateNewShortUrl();
        String appUrl = request.getScheme() + this.redirectBasePath;
        String fullShortUrl = appUrl + "/" + shortUrlPath;

        Link link = new Link(
                fullShortUrl,
                originalUrl,
                user
        );

        try {
            this.linkRepository.save(link);

            return new ResponseShortShortenLinkDto(
                    link.getShortUrl(),
                    link.getOriginalUrl()
            );
        } catch(DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Não foi possível salvar o link: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Houve um erro ao tentar criar um link", e);
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
        String fullShortUrl = this.redirectBasePath + shortUrl;
        Link link = this.linkRepository.findByShortUrl(fullShortUrl).orElseThrow(
                () -> new EntityNotFoundException("A shortUrl: " + shortUrl + " não foi encontrada!"));

        this.addClickInLink(link);
        return link.getOriginalUrl();
    }

    private void addClickInLink(Link link) {
        link.setClicks(link.getClicks() + 1);
        this.linkRepository.save(link);
    }

    public Page<Link> getAll(Pageable pageable) {
        return this.linkRepository.findAll(pageable);
    }

    public Page<Link> getAllByUserId(Long userId, Pageable pageable) {
        return this.linkRepository.findByUserId(userId, pageable);
    }

    public Link getByShortUrl(String shortUrl) {
        String fullShortUrl = this.redirectBasePath + shortUrl;
        return this.linkRepository.findByShortUrl(fullShortUrl).orElseThrow(
                () -> new EntityNotFoundException("A shortUrl: " + shortUrl + " não foi encontrada!"));
    }

    public void deleteByShortUrl(String shortUrl) {
        Link link = this.getByShortUrl(shortUrl);

        try {
            this.linkRepository.delete(link);
        } catch (Exception e) {
            logger.error("Aconteceu um erro ao tentar deletar o link com a shortUrl: {}", shortUrl, e);
            throw new DeleteFailedException("Aconteceu um erro ao tentar deletar o link com a shortUrl: " + shortUrl);
        }
    }
}

package com.gabrielrodrigues.encurtador_de_links.repositories;

import com.gabrielrodrigues.encurtador_de_links.models.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, Long> {
}

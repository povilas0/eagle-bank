package com.povilas.eagle_bank.user.persistence;

import com.povilas.eagle_bank.user.domain.User;
import com.povilas.eagle_bank.user.domain.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserPersistenceMapper mapper;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate, UserPersistenceMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public Optional<User> findById(String id) {
        return jdbcTemplate.query(
                """
                SELECT id, name, email, phone_number,
                    address_line1, address_line2, address_line3,
                    address_town, address_county, address_postcode,
                    created_timestamp, updated_timestamp
                FROM users WHERE id = ?
                """,
                (rs, rowNum) -> mapper.toDomain(mapper.toEntity(rs)),
                id
        ).stream().findFirst();
    }

    @Override
    public void save(User user) {
        UserEntity entity = mapper.toEntity(user);
        jdbcTemplate.update(
                """
                INSERT INTO users (id, name, email, phone_number,
                    address_line1, address_line2, address_line3,
                    address_town, address_county, address_postcode,
                    created_timestamp, updated_timestamp)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
                entity.id(),
                entity.name(),
                entity.email(),
                entity.phoneNumber(),
                entity.addressLine1(),
                entity.addressLine2(),
                entity.addressLine3(),
                entity.addressTown(),
                entity.addressCounty(),
                entity.addressPostcode(),
                entity.createdTimestamp(),
                entity.updatedTimestamp()
        );
    }
}

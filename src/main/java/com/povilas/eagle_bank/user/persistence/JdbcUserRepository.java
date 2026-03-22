package com.povilas.eagle_bank.user.persistence;

import com.povilas.eagle_bank.user.domain.User;
import com.povilas.eagle_bank.user.domain.UserRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserPersistenceMapper mapper;

    public JdbcUserRepository(NamedParameterJdbcTemplate jdbcTemplate, UserPersistenceMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public Optional<User> findById(String id) {
        var params = new MapSqlParameterSource("id", id);
        return jdbcTemplate.query(
                """
                SELECT id, name, email, phone_number,
                    address_line1, address_line2, address_line3,
                    address_town, address_county, address_postcode,
                    created_timestamp, updated_timestamp
                FROM users WHERE id = :id
                """,
                params,
                (rs, rowNum) -> mapper.toDomain(mapper.toEntity(rs))
        ).stream().findFirst();
    }

    @Override
    public void save(User user) {
        UserEntity entity = mapper.toEntity(user);
        var params = new MapSqlParameterSource()
                .addValue("id", entity.id())
                .addValue("name", entity.name())
                .addValue("email", entity.email())
                .addValue("phoneNumber", entity.phoneNumber())
                .addValue("addressLine1", entity.addressLine1())
                .addValue("addressLine2", entity.addressLine2())
                .addValue("addressLine3", entity.addressLine3())
                .addValue("addressTown", entity.addressTown())
                .addValue("addressCounty", entity.addressCounty())
                .addValue("addressPostcode", entity.addressPostcode())
                .addValue("createdTimestamp", entity.createdTimestamp())
                .addValue("updatedTimestamp", entity.updatedTimestamp());
        jdbcTemplate.update(
                """
                INSERT INTO users (id, name, email, phone_number,
                    address_line1, address_line2, address_line3,
                    address_town, address_county, address_postcode,
                    created_timestamp, updated_timestamp)
                VALUES (:id, :name, :email, :phoneNumber,
                    :addressLine1, :addressLine2, :addressLine3,
                    :addressTown, :addressCounty, :addressPostcode,
                    :createdTimestamp, :updatedTimestamp)
                """,
                params
        );
    }
}

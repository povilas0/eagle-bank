package com.povilas.eagle_bank.user.persistence;

import java.sql.Timestamp;

public record UserEntity(
        String id,
        String name,
        String email,
        String phoneNumber,
        String addressLine1,
        String addressLine2,
        String addressLine3,
        String addressTown,
        String addressCounty,
        String addressPostcode,
        Timestamp createdTimestamp,
        Timestamp updatedTimestamp
) {}

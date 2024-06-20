package com.example.mobileproject.model;

import com.example.mobileproject.dto.response.BookResponse;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category
{
    long id;
    String name;

}

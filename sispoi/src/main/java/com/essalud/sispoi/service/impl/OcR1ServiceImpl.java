package com.essalud.sispoi.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.essalud.sispoi.dto.OcR1ReportDTO;
import com.essalud.sispoi.service.IOcR1Service;

@Service
public class OcR1ServiceImpl implements IOcR1Service {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<OcR1ReportDTO> getOcR1Data(Integer dependencyId, Integer year, Integer modification) {
        String sql = "SELECT * FROM oc_r1(?, ?, ?)";
        
        return jdbcTemplate.query(sql, new OcR1RowMapper(), dependencyId, year, modification);
    }

    private static class OcR1RowMapper implements RowMapper<OcR1ReportDTO> {
        @Override
        public OcR1ReportDTO mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            OcR1ReportDTO dto = new OcR1ReportDTO();
            
            dto.setCodOe(rs.getString("cod_oe"));
            dto.setOe(rs.getString("oe"));
            dto.setCodAe(rs.getString("cod_ae"));
            dto.setAe(rs.getString("ae"));
            dto.setCodCg(rs.getString("cod_cg"));
            dto.setCg(rs.getString("cg"));
            dto.setCodCc(rs.getString("cod_cc"));
            dto.setCc(rs.getString("cc"));
            dto.setCodFf(rs.getString("cod_ff"));
            dto.setFf(rs.getString("ff"));
            dto.setCodAo(rs.getString("cod_ao"));
            dto.setAo(rs.getString("ao"));
            dto.setMu(rs.getString("mu"));
            dto.setI(rs.getBigDecimal("i"));
            dto.setIi(rs.getBigDecimal("ii"));
            dto.setIii(rs.getBigDecimal("iii"));
            dto.setIv(rs.getBigDecimal("iv"));
            dto.setTm(rs.getBigDecimal("tm"));
            dto.setRem(rs.getBigDecimal("rem"));
            dto.setBie(rs.getBigDecimal("bie"));
            dto.setServ(rs.getBigDecimal("serv"));
            dto.setTO(rs.getBigDecimal("t_o"));
            dto.setDa(rs.getString("da"));
            
            return dto;
        }
    }
}

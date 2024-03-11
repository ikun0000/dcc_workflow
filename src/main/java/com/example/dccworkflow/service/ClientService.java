package com.example.dccworkflow.service;

import com.example.dccworkflow.dto.ClientDto;
import com.example.dccworkflow.entity.Brand;
import com.example.dccworkflow.entity.Client;
import com.example.dccworkflow.entity.QClient;
import com.example.dccworkflow.exception.ClientNotFoundException;
import com.example.dccworkflow.repository.ClientRepository;
import com.example.dccworkflow.utils.LikeWrap;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {
    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public Client updateClient(Long id,
                               String clientId,
                               String clientName,
                               String clientAddress,
                               String managerName,
                               String phone1,
                               String phone2,
                               String phone3,
                               String company,
                               String taxNumber,
                               Long brandId,
                               String city,
                               Integer clientState,
                               String clientEmail,
                               String backupField1,
                               String backupField2,
                               String backupField3,
                               String note) throws ClientNotFoundException {
        Client client = new Client();
        if (id != null) {
            client = clientRepository.findById(id).orElseThrow(ClientNotFoundException::new);
        }

        if (clientId != null && !clientId.isBlank()) client.setClientId(clientId);
        if (clientName != null) client.setClientName(clientName);
        if (clientAddress != null) client.setClientAddress(clientAddress);
        if (managerName != null) client.setManagerName(managerName);
        if (phone1 != null) client.setPhone1(phone1);
        if (phone2 != null) client.setPhone2(phone2);
        if (phone3 != null) client.setPhone3(phone3);
        if (company != null) client.setCompany(company);
        if (taxNumber != null) client.setTaxNumber(taxNumber);
        if (brandId != null) {
            Brand brand = new Brand();
            brand.setId(brandId);
            client.setBrand(brand);
        }
        if (city != null) client.setCity(city);
        if (clientState != null) client.setClientState(clientState);
        if (clientEmail != null) client.setClientEmail(clientEmail);
        if (backupField1 != null) client.setBackupField1(backupField1);
        if (backupField2 != null) client.setBackupField2(backupField2);
        if (backupField3 != null) client.setBackupField3(backupField3);
        if (note != null) client.setNote(note);

        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public Page<ClientDto> getClientDto(String search, Pageable pageable) {
        QClient qClient = QClient.client;

        BooleanBuilder whereCase = new BooleanBuilder();
        if (search != null && !search.isBlank()) {
            whereCase.or(qClient.clientId.like(LikeWrap.like(search)));
            whereCase.or(qClient.clientName.like(LikeWrap.like(search)));
            whereCase.or(qClient.clientAddress.like(LikeWrap.like(search)));
        }

        Page<Client> clientPage = clientRepository.findAll(whereCase, pageable);

        List<ClientDto> content = clientPage.getContent()
                .stream()
                .map(client -> {
                    ClientDto dto = new ClientDto();
                    BeanUtils.copyProperties(client, dto);

                    dto.setBrandId(client.getBrand().getId());
                    dto.setBrandName(client.getBrand().getName());
                    dto.setBrandRef(client.getBrand().getRef());
                    return dto;
                })
                .toList();

        return new PageImpl<>(content, pageable, clientPage.getTotalElements());
    }
}

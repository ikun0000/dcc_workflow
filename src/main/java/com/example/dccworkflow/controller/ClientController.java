package com.example.dccworkflow.controller;

import com.example.dccworkflow.dto.*;
import com.example.dccworkflow.enums.ClientState;
import com.example.dccworkflow.enums.ResultType;
import com.example.dccworkflow.service.BrandService;
import com.example.dccworkflow.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/client")
public class ClientController {
    private ClientService clientService;
    private BrandService brandService;

    public ClientController(ClientService clientService, BrandService brandService) {
        this.clientService = clientService;
        this.brandService = brandService;
    }

    @GetMapping("/brandListPage")
    @PreAuthorize("hasAuthority('client_edit')")
    public String brandListPage() {
        return "client/brandList";
    }

    @GetMapping("/brandList.json")
    @ResponseBody
    public Result<List<BrandDto>> brandListJSON() {
        Page<BrandDto> brandDtos = brandService.getBrandDto(null, Pageable.unpaged());
        return Result.of(ResultType.SUCCESS, brandDtos.getContent());
    }

    @GetMapping("/brandList.bt")
    @PreAuthorize("hasAuthority('client_edit')")
    @ResponseBody
    public Result<BTResult<BrandDto>> brandListBT(@RequestParam(defaultValue = "") String search,
                                                  @RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        Page<BrandDto> brandPage = brandService.getBrandDto(search, PageRequest.of(page - 1, size));
        return Result.of(ResultType.SUCCESS,
                BTResult.of(brandPage.getContent(), brandPage.getTotalElements()));
    }

    @PostMapping("/addBrand")
    @PreAuthorize("hasAuthority('client_edit')")
    public RedirectView addBrand(@RequestParam String name,
                                 @RequestParam String ref) {
        brandService.addBrand(name, ref);
        return new RedirectView("/client/brandListPage");
    }

    @PostMapping("/removeBrand")
    @PreAuthorize("hasAuthority('client_edit')")
    @ResponseBody
    public Result<Object> removeBrand(@RequestParam Long brandId) {
        try {
            brandService.removeBrand(brandId);
        } catch (Exception e) {
            return Result.of(ResultType.BRAND_CONSTRAINT, null);
        }
        return Result.of(ResultType.SUCCESS, null);
    }

    @GetMapping("/clientListPage")
    @PreAuthorize("hasAuthority('client_edit')")
    public String clientListPage() {
        return "client/clientList";
    }

    @GetMapping("/clientList.bt")
    @PreAuthorize("hasAuthority('client_edit')")
    @ResponseBody
    public Result<BTResult<ClientDto>> clientListBT(@RequestParam(defaultValue = "") String search,
                                                    @RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        Page<ClientDto> clientDtos = clientService.getClientDto(search,
                PageRequest.of(page - 1, size,
                        Sort.by(Sort.Direction.DESC, "id")));
        return Result.of(ResultType.SUCCESS,
                BTResult.of(clientDtos.getContent(), clientDtos.getTotalElements()));
    }

    @PostMapping("/addClient")
    @PreAuthorize("hasAuthority('client_edit')")
    public RedirectView addClient(@RequestParam String clientId,
                                  @RequestParam String clientName,
                                  @RequestParam String clientAddress,
                                  @RequestParam Long brand,
                                  @RequestParam String city,
                                  String managerName,
                                  String clientEmail,
                                  String phone1,
                                  String phone2,
                                  String phone3,
                                  String company,
                                  String taxNumber,
                                  String backupField1,
                                  String backupField2,
                                  String backupField3,
                                  String note) {
        try {
            clientService.updateClient(null,
                    clientId,
                    clientName,
                    clientAddress,
                    managerName,
                    phone1,
                    phone2,
                    phone3,
                    company,
                    taxNumber,
                    brand,
                    city,
                    ClientState.NORMAL.getState(),
                    clientEmail,
                    backupField1,
                    backupField2,
                    backupField3,
                    note);
        } catch (Exception e) {
            return new RedirectView("/client/clientListPage?AddFail");
        }

        return new RedirectView("/client/clientListPage?success");
    }

    @PostMapping("/updateClient")
    @PreAuthorize("hasAuthority('client_edit')")
    public RedirectView updateClient(@RequestParam Long id,
                                      String clientId,
                                      @RequestParam String clientName,
                                      @RequestParam String clientAddress,
                                      @RequestParam Long brand,
                                      @RequestParam String city,
                                      String managerName,
                                      String clientEmail,
                                      String phone1,
                                      String phone2,
                                      String phone3,
                                      String company,
                                      String taxNumber,
                                      String backupField1,
                                      String backupField2,
                                      String backupField3,
                                      String note) {
        try {
            clientService.updateClient(id,
                    clientId,
                    clientName,
                    clientAddress,
                    managerName,
                    phone1,
                    phone2,
                    phone3,
                    company,
                    taxNumber,
                    brand,
                    city,
                    null,
                    clientEmail,
                    backupField1,
                    backupField2,
                    backupField3,
                    note);
        } catch (Exception e) {
            return new RedirectView("/client/clientListPage?UpdateFail");
        }

        return new RedirectView("/client/clientListPage?success");
    }

    @GetMapping("/clientList.json")
    @ResponseBody
    public Result<List<ClientDto>> clientListJSON() {
        Page<ClientDto> clientDto = clientService.getClientDto(null, Pageable.unpaged());
        return Result.of(ResultType.SUCCESS, clientDto.getContent());
    }
}

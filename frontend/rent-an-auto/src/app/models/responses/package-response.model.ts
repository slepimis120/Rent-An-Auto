import {CompanyResponse} from "./company-response.model";

export interface PackageResponse {
    id: string,
    price: number,
    name: string,
    description: string,
    validFor: number,
    company: CompanyResponse
}

import {PackageResponse} from "./package-response.model";
import {UserResponse} from "./user-response.model";

export interface PurchasedPackageResponse {
    id: string,
    packageDetails: PackageResponse,
    purchasedBy: UserResponse,
    validFrom: string,
    validUntil: string
}

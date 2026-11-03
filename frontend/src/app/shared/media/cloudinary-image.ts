const CLOUDINARY_UPLOAD_SEGMENT = '/image/upload/';
const PRODUCT_CARD_TRANSFORMATION = 'c_pad,w_600,h_600,b_white,q_auto,f_auto';

export function productCardImageUrl(url: string | null | undefined, fallback: string): string {
  if (!url) return fallback;
  if (!url.includes('res.cloudinary.com') || !url.includes(CLOUDINARY_UPLOAD_SEGMENT)) return url;

  return url.replace(
    CLOUDINARY_UPLOAD_SEGMENT,
    `${CLOUDINARY_UPLOAD_SEGMENT}${PRODUCT_CARD_TRANSFORMATION}/`
  );
}
